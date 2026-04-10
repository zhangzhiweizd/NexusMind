-- NexusMind 数据库表注释初始化脚本
-- 用于 PostgreSQL 数据库，在表创建后执行

-- ====================================
-- 用户表注释
-- ====================================
COMMENT ON TABLE "user" IS '用户表';
COMMENT ON COLUMN "user".id IS '主键ID';
COMMENT ON COLUMN "user".username IS '用户名';
COMMENT ON COLUMN "user".password IS '密码';
COMMENT ON COLUMN "user".email IS '邮箱';
COMMENT ON COLUMN "user".nickname IS '昵称';
COMMENT ON COLUMN "user".phone IS '手机号';
COMMENT ON COLUMN "user".avatar IS '头像URL';
COMMENT ON COLUMN "user".enabled IS '是否启用';
COMMENT ON COLUMN "user".created_at IS '创建时间';
COMMENT ON COLUMN "user".updated_at IS '更新时间';

-- ====================================
-- 角色表注释
-- ====================================
COMMENT ON TABLE role IS '角色表';
COMMENT ON COLUMN role.id IS '主键ID';
COMMENT ON COLUMN role.name IS '角色名称';
COMMENT ON COLUMN role.description IS '角色描述';
COMMENT ON COLUMN role.created_at IS '创建时间';
COMMENT ON COLUMN role.updated_at IS '更新时间';

-- ====================================
-- 用户角色关联表注释
-- ====================================
COMMENT ON TABLE user_role IS '用户角色关联表';
COMMENT ON COLUMN user_role.user_id IS '用户ID';
COMMENT ON COLUMN user_role.role_id IS '角色ID';

-- ====================================
-- 工作空间表注释
-- ====================================
COMMENT ON TABLE workspace IS '工作空间表';
COMMENT ON COLUMN workspace.id IS '主键ID';
COMMENT ON COLUMN workspace.name IS '工作空间名称';
COMMENT ON COLUMN workspace.description IS '工作空间描述';
COMMENT ON COLUMN workspace.creator_id IS '创建者ID';
COMMENT ON COLUMN workspace.created_at IS '创建时间';
COMMENT ON COLUMN workspace.updated_at IS '更新时间';

-- ====================================
-- 工作空间成员表注释
-- ====================================
COMMENT ON TABLE workspace_member IS '工作空间成员表';
COMMENT ON COLUMN workspace_member.workspace_id IS '工作空间ID';
COMMENT ON COLUMN workspace_member.user_id IS '用户ID';

-- ====================================
-- 文件夹表注释
-- ====================================
COMMENT ON TABLE folder IS '文件夹表';
COMMENT ON COLUMN folder.id IS '主键ID';
COMMENT ON COLUMN folder.name IS '文件夹名称';
COMMENT ON COLUMN folder.description IS '文件夹描述';
COMMENT ON COLUMN folder.workspace_id IS '所属工作空间ID';
COMMENT ON COLUMN folder.parent_id IS '父文件夹ID';
COMMENT ON COLUMN folder.created_at IS '创建时间';
COMMENT ON COLUMN folder.updated_at IS '更新时间';

-- ====================================
-- 文档表注释
-- ====================================
COMMENT ON TABLE document IS '文档表';
COMMENT ON COLUMN document.id IS '主键ID';
COMMENT ON COLUMN document.title IS '文档标题';
COMMENT ON COLUMN document.content IS '文档内容（JSONB格式，存储富文本内容）';
COMMENT ON COLUMN document.plain_text IS '纯文本内容，用于搜索';
COMMENT ON COLUMN document.folder_id IS '所属文件夹ID';
COMMENT ON COLUMN document.creator_id IS '创建者ID';
COMMENT ON COLUMN document.status IS '状态：DRAFT-草稿, PUBLISHED-已发布, ARCHIVED-已归档';
COMMENT ON COLUMN document.tags IS '标签';
COMMENT ON COLUMN document.version IS '版本号，用于乐观锁';
COMMENT ON COLUMN document.created_at IS '创建时间';
COMMENT ON COLUMN document.updated_at IS '更新时间';

-- ====================================
-- 文件附件表注释
-- ====================================
COMMENT ON TABLE file_attachment IS '文件附件表';
COMMENT ON COLUMN file_attachment.id IS '主键ID';
COMMENT ON COLUMN file_attachment.original_name IS '原始文件名';
COMMENT ON COLUMN file_attachment.stored_name IS '存储文件名';
COMMENT ON COLUMN file_attachment.file_path IS '文件路径';
COMMENT ON COLUMN file_attachment.file_size IS '文件大小（字节）';
COMMENT ON COLUMN file_attachment.content_type IS '文件MIME类型';
COMMENT ON COLUMN file_attachment.file_type IS '文件类型：IMAGE, VIDEO, DOCUMENT, OTHER';
COMMENT ON COLUMN file_attachment.extension IS '文件扩展名';
COMMENT ON COLUMN file_attachment.uploader_id IS '上传者ID';
COMMENT ON COLUMN file_attachment.document_id IS '关联的文档ID';
COMMENT ON COLUMN file_attachment.access_url IS '访问URL';
COMMENT ON COLUMN file_attachment.is_public IS '是否公开访问';
COMMENT ON COLUMN file_attachment.download_count IS '下载次数';
COMMENT ON COLUMN file_attachment.created_at IS '创建时间';
