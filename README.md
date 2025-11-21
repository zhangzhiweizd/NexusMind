# NexusMind （智慧连接）
````
含义：Nexus（连接点）+ Mind（智慧）。寓意你的知识像神经网络一样连接，智慧在其中流动。AI 与知识的交汇点
寓意：充满连接、智慧与无限可能，是未来知识管理的枢纽。
适合：如果你希望它成为个人知识的“大脑”。
````
### 🧩核心功能
| 功能 | 说明 | 实现建议 |
|-----|-----|-----|
|1.创建笔记|用户输入标题+纯文本就可以了|前端表单+localStorage存储|
|2.AI自动摘要|点击按钮，调用大模型生成|如：通|
|3.查看笔记列表|展示所有笔记标题|从localStorage读取渲染|
|4.语译搜索|输入关键词，返回相关笔记（先用关键词匹配，后续升级向量搜索）|前端 JS 模糊搜索（如 Fuse.js）|
### 🛠 技术栈推荐（兼顾新潮 & 实用 & 国内友好）
|层级|技术选型|为什么选择|
|----|-----|----|
|前端|React+Vite+Tailwind Css|快速启动、现代语法、样式高效；你作为 Java 开发者也容易上手|
|AI模型|通义APi|暂时好用|
|存储|浏览器 localStorage|MVP 阶段无需后端！避免数据库/用户系统复杂度|
|部署|	Vercel / GitHub Pages|免费、一键部署静态站，5 分钟上线|
|后续扩展|Spring Boot + PostgreSQL + Milvus	|等 MVP 验证成功后再加后端|
### 📁 项目结构建议（前端-only）
```text
nexusmind/
├── public/
├── src/
│   ├── components/
│   │   ├── NoteForm.jsx      # 笔记输入表单
│   │   ├── NoteList.jsx      # 笔记列表
│   │   └── AISummary.jsx     # 显示 AI 摘要
│   ├── services/
│   │   └── qwenService.js    # 封装 Qwen API 调用
│   ├── utils/
│   │   └── storage.js        # localStorage 封装
│   ├── App.jsx
│   └── main.jsx
├── index.html
├── package.json
└── tailwind.config.js

```
