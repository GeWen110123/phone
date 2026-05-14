<template>
  <div>
    <!-- 聊天内容弹框 -->
    <a-modal
      title="评论区聊天"
      :visible="open"
      @cancel="open = false"
      width="800px"
      :footer="null"
      destroyOnClose
    >
      <!-- 聊天列表容器 -->
      <div class="chat-container">
        <div class="chat-item" v-for="item in chatList" :key="item.id">
          <!-- 头像区域 -->
          <div class="chat-avatar">
            <a-avatar
              :size="48"
              :style="{ backgroundColor: getRandomColor(item.id), color: '#fff' }"
            >
              {{ getShortNickname(item.nickname) }}
            </a-avatar>
          </div>
          <!-- 消息内容包装器 -->
          <div class="chat-content-wrapper">
            <!-- 回复引用 -->
            <div v-if="item.replyTo" class="chat-reply-quote">
              <i class="el-icon-chat-line-round quote-icon"></i>
              <span class="quote-label">@{{ item.replyTo }}：</span>
              <span class="quote-text">{{ getReplyContent(item.replyTo) }}</span>
            </div>
            <!-- 消息内容区域 -->
            <div class="chat-content">
              <!-- 用户名和时间行 -->
              <div class="chat-header">
                <span class="nickname">{{ item.nickname || "匿名用户" }}</span>
                <span class="chat-time">{{ item.time }}</span>
              </div>
              <!-- 消息文本 -->
              <div class="chat-text">{{ item.text }}</div>
            </div>
          </div>
        </div>
      </div>
    </a-modal>
  </div>
</template>

<script>
export default {
  name: "ChatModal",
  data() {
    return {
      open: false,
      chatList: [],
    };
  },
  methods: {
    showChatModal(contentStr) {
      try {
        this.chatList = JSON.parse(contentStr);
      } catch (e) {
        console.error('解析聊天数据失败:', e);
        this.chatList = [];
      }
      console.log(this.chatList);
      this.open = true;
    },

    // 生成随机颜色（基于id做哈希，使同一用户颜色固定）
    getRandomColor(id) {
      const colors = [
        // 蓝色系
        "#4285F4", "#3498db", "#2980b9", "#00bcd4", "#0097a7", "#607d8b",
        // 红色/粉色系
        "#EA4335", "#e74c3c", "#c0392b", "#e91e63", "#c2185b", "#ff6b6b",
        // 黄色/橙色系
        "#FBBC05", "#f1c40f", "#f39c12", "#FF9800", "#e67e22", "#d35400",
        // 绿色系
        "#34A853", "#2ecc71", "#27ae60", "#16a085", "#1abc9c", "#26c6da",
        // 紫色系
        "#9C27B0", "#8e44ad", "#6c3483", "#9b59b6", "#7b1fa2", "#ba68c8",
        // 棕色/灰色系
        "#795548", "#8d6e63", "#5d4037", "#95a5a6", "#7f8c8d", "#bdc3c7",
        // 其他柔和色
        "#ff7675", "#fd79a8", "#fdcb6e", "#a29bfe", "#00b894", "#e17055"
      ];
      return colors[id % colors.length];
    },

    // 截取昵称前n个字（这里设为2个，可调整）
    getShortNickname(nickname) {
      if (!nickname) return "匿";
      const maxLen = 2;
      return nickname.length > maxLen
        ? nickname.substring(0, maxLen)
        : nickname;
    },

    // 模拟获取回复内容（简化版）
    getReplyContent(replyTo) {
      const target = this.chatList.find(item => item.nickname === replyTo);
      return target ? (target.text.length > 15 ? target.text.substring(0, 15) + '...' : target.text) : '暂无内容';
    },
  },
};
</script>

<style scoped>
/* 聊天容器样式 */
.chat-container {
  max-height: 600px;
  overflow-y: auto;
  padding: 15px;
  background-color: #f8f9fa;
  border-radius: 12px;
}

/* 单个聊天项样式 */
.chat-item {
  display: flex;
  margin-bottom: 20px;
  align-items: flex-start;
}

/* 头像样式 */
.chat-avatar {
  margin-right: 12px;
  flex-shrink: 0;
}

/* 消息内容包装器 */
.chat-content-wrapper {
  flex: 1;
}

/* 回复引用样式 */
.chat-reply-quote {
  display: flex;
  align-items: flex-start;
  padding: 8px 12px;
  background-color: #f0f2f5;
  border-radius: 8px;
  font-size: 12px;
  color: #666;
  margin-bottom: 8px;
  border-left: 3px solid #4285F4;
}

.quote-icon {
  font-size: 14px;
  color: #4285F4;
  margin-right: 6px;
  margin-top: 1px;
}

.quote-label {
  font-weight: 600;
  color: #999;
  margin-right: 4px;
}

/* 消息内容区域 */
.chat-content {
  padding: 12px 16px;
  background-color: #ffffff;
  border-radius: 12px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.05);
  border: 1px solid #f0f0f0;
}

/* 用户名和时间行 */
.chat-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 6px;
}

.nickname {
  font-weight: 600;
  color: #333;
  font-size: 14px;
}

.chat-time {
  font-size: 11px;
  color: #999;
}

/* 消息文本 */
.chat-text {
  font-size: 14px;
  color: #333;
  line-height: 1.5;
  word-wrap: break-word;
}

/* 滚动条美化 */
.chat-container::-webkit-scrollbar {
  width: 8px;
}

.chat-container::-webkit-scrollbar-thumb {
  background-color: #ddd;
  border-radius: 4px;
}

.chat-container::-webkit-scrollbar-track {
  background-color: #f8f9fa;
  border-radius: 4px;
}

/* 最后一个聊天项取消下间距 */
.chat-item:last-child {
  margin-bottom: 0;
}
</style>