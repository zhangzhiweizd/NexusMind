package com.iop.nexusmind.service;

import com.iop.nexusmind.dto.NoteDTO;
import com.vladsch.flexmark.html2md.converter.FlexmarkHtmlConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 笔记导出服务
 * 支持导出为 Markdown 和 Word 格式
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ExportService {

    private final NoteService noteService;

    /**
     * 导出单个笔记为 Markdown 格式
     * @param noteId 笔记 ID
     * @return Markdown 内容字符串
     */
    public String exportToMarkdown(Long noteId) {
        NoteDTO note = noteService.getNoteById(noteId);
        return convertToMarkdown(note);
    }

    /**
     * 导出多个笔记为 Markdown 格式（打包成 ZIP）
     * @param noteIds 笔记 ID 列表
     * @return ZIP 文件的字节数组
     */
    public byte[] exportMultipleToMarkdown(List<Long> noteIds) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ZipOutputStream zos = new ZipOutputStream(baos)) {
            
            for (Long noteId : noteIds) {
                NoteDTO note = noteService.getNoteById(noteId);
                String markdownContent = convertToMarkdown(note);
                
                // 创建安全的文件名（移除特殊字符）
                String fileName = sanitizeFileName(note.getTitle()) + ".md";
                
                ZipEntry entry = new ZipEntry(fileName);
                zos.putNextEntry(entry);
                zos.write(markdownContent.getBytes("UTF-8"));
                zos.closeEntry();
            }
            
            zos.finish();
            return baos.toByteArray();
        }
    }

    /**
     * 导出单个笔记为 Word 格式
     * 解析 HTML 内容，保留格式、列表、图片等
     * @param noteId 笔记 ID
     * @return Word 文件的字节数组
     */
    public byte[] exportToWord(Long noteId) throws IOException {
        NoteDTO note = noteService.getNoteById(noteId);
        
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            XWPFDocument document = new XWPFDocument();
            
            // 创建标题段落
            XWPFParagraph titleParagraph = document.createParagraph();
            titleParagraph.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun titleRun = titleParagraph.createRun();
            titleRun.setText(note.getTitle());
            titleRun.setBold(true);
            titleRun.setFontSize(18);
            titleRun.setFontFamily("微软雅黑");
            
            // 创建摘要段落
            if (note.getSummary() != null && !note.getSummary().isEmpty()) {
                XWPFParagraph summaryParagraph = document.createParagraph();
                summaryParagraph.setAlignment(ParagraphAlignment.LEFT);
                XWPFRun summaryRun = summaryParagraph.createRun();
                summaryRun.setText("摘要：" + note.getSummary());
                summaryRun.setItalic(true);
                summaryRun.setFontSize(12);
            }
            
            // 创建分类和标签信息
            XWPFParagraph metaParagraph = document.createParagraph();
            metaParagraph.setAlignment(ParagraphAlignment.LEFT);
            XWPFRun metaRun = metaParagraph.createRun();
            if (note.getCategoryName() != null) {
                metaRun.setText("分类：" + note.getCategoryName() + "    ");
            }
            if (note.getTagIds() != null && !note.getTagIds().isEmpty()) {
                StringBuilder tags = new StringBuilder("标签：");
                // 这里简化处理，只显示标签 ID，实际应用中可以查询标签名称
                note.getTagIds().forEach(tagId -> tags.append("Tag-").append(tagId).append(", "));
                metaRun.setText(tags.substring(0, tags.length() - 2));
            }
            
            // 解析并添加 HTML 内容
            if (note.getContent() != null && !note.getContent().isEmpty()) {
                addHtmlContentToWord(document, note.getContent());
            }
            
            // 创建创建时间
            XWPFParagraph dateParagraph = document.createParagraph();
            dateParagraph.setAlignment(ParagraphAlignment.RIGHT);
            XWPFRun dateRun = dateParagraph.createRun();
            dateRun.setText("创建时间：" + note.getCreatedAt());
            dateRun.setFontSize(10);
            dateRun.setItalic(true);
            
            document.write(baos);
            return baos.toByteArray();
        }
    }

    /**
     * 导出多个笔记为 Word 格式（打包成 ZIP）
     * @param noteIds 笔记 ID 列表
     * @return ZIP 文件的字节数组
     */
    public byte[] exportMultipleToWord(List<Long> noteIds) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ZipOutputStream zos = new ZipOutputStream(baos)) {
            
            for (Long noteId : noteIds) {
                NoteDTO note = noteService.getNoteById(noteId);
                byte[] wordContent = exportToWord(noteId);
                
                // 创建安全的文件名
                String fileName = sanitizeFileName(note.getTitle()) + ".docx";
                
                ZipEntry entry = new ZipEntry(fileName);
                zos.putNextEntry(entry);
                zos.write(wordContent);
                zos.closeEntry();
            }
            
            zos.finish();
            return baos.toByteArray();
        }
    }

    /**
     * 将笔记转换为 Markdown 格式
     * 使用 Flexmark 将 HTML 转换为 Markdown，保留所有格式和图片
     */
    private String convertToMarkdown(NoteDTO note) {
        StringBuilder md = new StringBuilder();
        
        // 标题
        md.append("# ").append(note.getTitle()).append("\n\n");
        
        // 摘要
        if (note.getSummary() != null && !note.getSummary().isEmpty()) {
            md.append("**摘要**：").append(note.getSummary()).append("\n\n");
        }
        
        // 元信息
        md.append("---\n\n");
        if (note.getCategoryName() != null) {
            md.append("**分类**：").append(note.getCategoryName()).append("\n\n");
        }
        if (note.getTagIds() != null && !note.getTagIds().isEmpty()) {
            StringBuilder tags = new StringBuilder("**标签**：");
            // 这里简化处理，只显示标签 ID
            note.getTagIds().forEach(tagId -> tags.append("`Tag-").append(tagId).append("` "));
            md.append(tags).append("\n\n");
        }
        
        // 内容 - 将 HTML 转换为 Markdown
        md.append("## 内容\n\n");
        if (note.getContent() != null && !note.getContent().isEmpty()) {
            try {
                // 配置 Flexmark 转换器，确保图片正确转换
                FlexmarkHtmlConverter converter = FlexmarkHtmlConverter.builder()
                    .build();
                String markdownContent = converter.convert(note.getContent());
                md.append(markdownContent);
            } catch (Exception e) {
                // 如果转换失败，直接使用原始内容
                log.warn("Markdown 转换失败，使用原始 HTML 内容：{}", e.getMessage());
                md.append(note.getContent()).append("\n");
            }
        }
        
        // 时间信息
        md.append("---\n\n");
        md.append("*创建时间：").append(note.getCreatedAt()).append("*\n");
        md.append("*更新时间：").append(note.getUpdatedAt()).append("*\n");
        
        return md.toString();
    }

    /**
     * 清理文件名中的特殊字符
     */
    private String sanitizeFileName(String fileName) {
        return fileName.replaceAll("[\\\\/:*?\"<>|]", "_")
                      .replaceAll("\\s+", "_");
    }

    /**
     * 解析 HTML 内容并添加到 Word 文档
     * 支持标题、段落、列表、加粗、斜体、图片等
     */
    private void addHtmlContentToWord(XWPFDocument document, String htmlContent) throws IOException {
        Document doc = Jsoup.parse(htmlContent);
        log.info("开始解析 HTML 内容，HTML 长度：{}", htmlContent.length());
        
        // 查找所有 img 标签
        Elements images = doc.select("img");
        log.info("找到 {} 张图片", images.size());
        for (Element img : images) {
            log.info("图片 src: {}", img.attr("src").substring(0, Math.min(50, img.attr("src").length())));
        }
        
        // 处理所有块级元素
        Elements elements = doc.body().children();
        for (Element element : elements) {
            processElement(document, element);
        }
    }

    /**
     * 递归处理 HTML 元素
     */
    private void processElement(XWPFDocument document, Element element) {
        String tagName = element.tagName();
        
        switch (tagName) {
            case "h1":
            case "h2":
            case "h3":
            case "h4":
            case "h5":
            case "h6":
                addHeadingToWord(document, element, tagName);
                break;
            case "p":
                addParagraphToWord(document, element);
                break;
            case "ul":
                addListToWord(document, element, false);
                break;
            case "ol":
                addListToWord(document, element, true);
                break;
            case "blockquote":
                addBlockquoteToWord(document, element);
                break;
            case "pre":
            case "code":
                addCodeBlockToWord(document, element);
                break;
            case "img":
                addImageToWord(document, element);
                break;
            case "br":
                // 换行已在段落中处理
                break;
            default:
                // 处理其他内联元素
                if (element.hasText()) {
                    addParagraphToWord(document, element);
                }
        }
    }

    /**
     * 添加标题到 Word
     */
    private void addHeadingToWord(XWPFDocument document, Element element, String tagName) {
        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.LEFT);
        XWPFRun run = paragraph.createRun();
        
        String text = element.text();
        run.setText(text);
        run.setBold(true);
        
        // 根据标题级别设置字体大小
        int fontSize = 16;
        switch (tagName) {
            case "h1": fontSize = 24; break;
            case "h2": fontSize = 22; break;
            case "h3": fontSize = 20; break;
            case "h4": fontSize = 18; break;
            case "h5": fontSize = 16; break;
            case "h6": fontSize = 14; break;
        }
        run.setFontSize(fontSize);
        run.setFontFamily("微软雅黑");
    }

    /**
     * 添加段落到 Word
     */
    private void addParagraphToWord(XWPFDocument document, Element element) {
        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.LEFT);
        
        // 递归处理子元素，保留格式
        addInlineContentToParagraph(paragraph, element);
    }

    /**
     * 在段落中添加图片
     */
    private void addImageToParagraph(XWPFParagraph paragraph, Element imgElement) {
        String src = imgElement.attr("src");
        if (src == null || src.isEmpty()) {
            return;
        }
        
        try {
            byte[] imageData = null;
            int pictureType = XWPFDocument.PICTURE_TYPE_PNG; // 默认 PNG
            String imageExtension = "png";
            
            // 处理 Base64 图片
            if (src.startsWith("data:image")) {
                // 提取图片类型
                if (src.contains("image/jpeg") || src.contains("image/jpg")) {
                    pictureType = XWPFDocument.PICTURE_TYPE_JPEG;
                    imageExtension = "jpg";
                } else if (src.contains("image/gif")) {
                    pictureType = XWPFDocument.PICTURE_TYPE_GIF;
                    imageExtension = "gif";
                }
                
                String base64Data = src.substring(src.indexOf(",") + 1);
                imageData = java.util.Base64.getDecoder().decode(base64Data);
            } else if (src.startsWith("http")) {
                // 远程图片：下载
                java.net.URL url = new java.net.URL(src);
                try (java.io.InputStream is = url.openStream()) {
                    imageData = is.readAllBytes();
                    
                    // 根据 URL 判断图片类型
                    if (src.contains(".jpg") || src.contains(".jpeg")) {
                        pictureType = XWPFDocument.PICTURE_TYPE_JPEG;
                        imageExtension = "jpg";
                    } else if (src.contains(".gif")) {
                        pictureType = XWPFDocument.PICTURE_TYPE_GIF;
                        imageExtension = "gif";
                    }
                }
            } else {
                // 本地路径，跳过
                log.warn("不支持本地图片路径：{}", src);
                return;
            }
            
            if (imageData == null || imageData.length == 0) {
                log.warn("图片数据为空：{}", src);
                return;
            }
            
            log.info("段落图片：Base64 解码成功，图片大小：{} bytes", imageData.length);
            
            // 在段落中创建图片 Run
            XWPFRun run = paragraph.createRun();
            
            // 使用正确的 API 添加图片
            run.addPicture(
                new java.io.ByteArrayInputStream(imageData),
                pictureType,
                "image." + imageExtension,
                400 * 9525, // 宽度（EMU 单位，约 400 像素）
                300 * 9525  // 高度（EMU 单位，约 300 像素）
            );
            
            log.info("成功添加图片到段落中：{}", src.substring(0, Math.min(50, src.length())));
        } catch (Exception e) {
            log.error("段落中添加图片失败：{}", e.getMessage(), e);
            // 图片加载失败，添加文字说明
            XWPFRun run = paragraph.createRun();
            String altText = imgElement.attr("alt");
            run.setText("[图片" + (altText.isEmpty() ? "" : ": " + altText) + "]");
            run.setItalic(true);
        }
    }

    /**
     * 添加内联内容（保留加粗、斜体等格式）
     */
    private void addInlineContentToParagraph(XWPFParagraph paragraph, Element element) {
        for (org.jsoup.nodes.Node node : element.childNodes()) {
            if (node instanceof org.jsoup.nodes.TextNode) {
                XWPFRun run = paragraph.createRun();
                run.setText(((org.jsoup.nodes.TextNode) node).text());
                run.setFontSize(12);
                run.setFontFamily("宋体");
            } else if (node instanceof Element) {
                Element childElement = (Element) node;
                String tagName = childElement.tagName();
                
                // 特殊处理图片标签
                if ("img".equals(tagName)) {
                    addImageToParagraph(paragraph, childElement);
                    continue;
                }
                
                XWPFRun run = paragraph.createRun();
                String text = childElement.text();
                
                // 处理格式
                switch (tagName) {
                    case "b":
                    case "strong":
                        run.setBold(true);
                        break;
                    case "i":
                    case "em":
                        run.setItalic(true);
                        break;
                    case "u":
                        run.setUnderline(UnderlinePatterns.SINGLE);
                        break;
                    case "s":
                    case "strike":
                        run.setStrikeThrough(true);
                        break;
                    case "sub":
                        run.setSubscript(VerticalAlign.SUBSCRIPT);
                        break;
                    case "sup":
                        run.setSubscript(VerticalAlign.SUPERSCRIPT);
                        break;
                }
                
                run.setText(text);
                run.setFontSize(12);
                run.setFontFamily("宋体");
                
                // 递归处理嵌套元素
                if (!childElement.childNodes().isEmpty()) {
                    addInlineContentToParagraph(paragraph, childElement);
                }
            }
        }
    }

    /**
     * 添加列表到 Word
     */
    private void addListToWord(XWPFDocument document, Element element, boolean ordered) {
        Elements items = element.children();
        int index = 1;
        
        for (Element item : items) {
            if ("li".equals(item.tagName())) {
                XWPFParagraph paragraph = document.createParagraph();
                paragraph.setAlignment(ParagraphAlignment.LEFT);
                
                XWPFRun run = paragraph.createRun();
                if (ordered) {
                    run.setText((index++) + ". " + item.text());
                } else {
                    run.setText("• " + item.text());
                }
                run.setFontSize(12);
                run.setFontFamily("宋体");
            }
        }
    }

    /**
     * 添加引用块到 Word
     */
    private void addBlockquoteToWord(XWPFDocument document, Element element) {
        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.LEFT);
        paragraph.setIndentationLeft(720); // 缩进
        
        XWPFRun run = paragraph.createRun();
        run.setText(element.text());
        run.setItalic(true);
        run.setFontSize(12);
        run.setFontFamily("宋体");
        run.setColor("666666"); // 灰色文字
    }

    /**
     * 添加代码块到 Word
     */
    private void addCodeBlockToWord(XWPFDocument document, Element element) {
        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.LEFT);
        paragraph.setIndentationLeft(360);
        
        XWPFRun run = paragraph.createRun();
        run.setText(element.text());
        run.setFontSize(10);
        run.setFontFamily("Courier New");
        run.setColor("000000");
        run.setBold(true);
    }

    /**
     * 添加图片到 Word（仅支持 Base64 或远程图片）
     */
    private void addImageToWord(XWPFDocument document, Element element) {
        String src = element.attr("src");
        if (src == null || src.isEmpty()) {
            return;
        }
        
        try {
            byte[] imageData = null;
            int pictureType = XWPFDocument.PICTURE_TYPE_PNG; // 默认 PNG
            String imageExtension = "png";
            
            // 处理 Base64 图片
            if (src.startsWith("data:image")) {
                // 提取图片类型
                if (src.contains("image/jpeg") || src.contains("image/jpg")) {
                    pictureType = XWPFDocument.PICTURE_TYPE_JPEG;
                    imageExtension = "jpg";
                } else if (src.contains("image/gif")) {
                    pictureType = XWPFDocument.PICTURE_TYPE_GIF;
                    imageExtension = "gif";
                }
                
                String base64Data = src.substring(src.indexOf(",") + 1);
                imageData = java.util.Base64.getDecoder().decode(base64Data);
            } else if (src.startsWith("http")) {
                // 远程图片：下载
                java.net.URL url = new java.net.URL(src);
                try (java.io.InputStream is = url.openStream()) {
                    imageData = is.readAllBytes();
                    
                    // 根据 URL 判断图片类型
                    if (src.contains(".jpg") || src.contains(".jpeg")) {
                        pictureType = XWPFDocument.PICTURE_TYPE_JPEG;
                        imageExtension = "jpg";
                    } else if (src.contains(".gif")) {
                        pictureType = XWPFDocument.PICTURE_TYPE_GIF;
                        imageExtension = "gif";
                    }
                }
            } else {
                // 本地路径，跳过
                log.warn("不支持本地图片路径：{}", src);
                return;
            }
            
            if (imageData == null || imageData.length == 0) {
                log.warn("图片数据为空：{}", src);
                return;
            }
            
            log.info("Base64 解码成功，图片大小：{} bytes", imageData.length);
            log.info("图片类型：{}, 扩展名：{}", pictureType, imageExtension);
            
            // 添加图片到 Word
            XWPFParagraph paragraph = document.createParagraph();
            paragraph.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun run = paragraph.createRun();
            
            log.info("准备调用 addPicture() 方法...");
            
            // 使用正确的 API 添加图片
            try {
                run.addPicture(
                    new java.io.ByteArrayInputStream(imageData),
                    pictureType,
                    "image." + imageExtension,
                    400 * 9525, // 宽度（EMU 单位，约 400 像素）
                    300 * 9525  // 高度（EMU 单位，约 300 像素）
                );
                log.info("addPicture() 调用成功！");
            } catch (Exception picException) {
                log.error("addPicture() 抛出异常：{}", picException.getMessage(), picException);
                throw picException; // 重新抛出以便捕获
            }
            
            log.info("成功添加图片到 Word: {}", src);
        } catch (Exception e) {
            log.error("添加图片失败：{}", e.getMessage(), e);
            // 图片加载失败，添加文字说明
            XWPFParagraph paragraph = document.createParagraph();
            XWPFRun run = paragraph.createRun();
            String altText = element.attr("alt");
            run.setText("[图片" + (altText.isEmpty() ? "" : ": " + altText) + "]");
            run.setItalic(true);
        }
    }
}
