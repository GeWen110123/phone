<template>
  <div class="video-list-container">
    <div class="page-header-wrapper">
      <!-- 用户详情卡片 -->
      <a-card class="user-info-card" v-show="userInfo.nickname" hoverable>
        <div class="user-info-wrap">
          <div class="user-avatar">
            <a-avatar :size="120" :src="getUrl(userInfo.touxiang)"></a-avatar>
          </div>
          <div class="user-base-info">
            <h3 class="user-nickname">
              {{ userInfo.nickname || "未知用户" }}
              <span style="color: purple; font-size: 14px"
                >{{ userInfo.real_name ? " (" + userInfo.real_name + ")" : "" }}
              </span>
            </h3>
            <p class="user-gender" v-if="userInfo.gender">
              {{ userInfo.gender }}
            </p>
            <div class="user-meta">
              <span class="meta-item">
                <a-icon type="user-add" /> 关注数：{{ userInfo.follow_count }}
              </span>
              <span class="meta-item">
                <a-icon type="star" /> 粉丝数：{{ userInfo.fans_count }}
              </span>
              <span class="meta-item">
                <a-icon type="like" /> 获赞数：{{ userInfo.likes_count }}
              </span>
            </div>
          </div>
          <div class="user-stats">
            <div class="user-item">
              <span class="user-label">作品数</span>
              <span class="user-value">{{ userInfo.works_count }}</span>
            </div>
            <div class="user-item">
              <span class="user-label">IP属地</span>
              <span class="user-value">{{ userInfo.region || "未知" }}</span>
            </div>
            <div class="user-item">
              <span class="user-label">获赞数</span>
              <span class="user-value">{{ userInfo.signature || "无" }}</span>
            </div>
          </div>
        </div>
      </a-card>
    </div>

    <a-spin :spinning="loading">
      <div class="video-card-wrap">
        <a-card
          v-for="(video, index) in videoList"
          :key="video.id"
          class="video-card"
          hoverable
        >
          <div class="card-content">
            <!-- 左侧视频区域 -->
            <div class="left-section">
              <div class="video-player-wrap">
                <video
                  ref="plyrList"
                  controls
                  :data-video-id="video.id"
                  preload="none"
                  class="video-player"
                  playsinline
                  webkit-playsinline
                  :poster="getUrl(video.imagePath)"
                  @play="handleVideoPlay(video.id)"
                >
                  <source :src="getUrl(video.videoPath)" type="video/mp4" />
                </video>
              </div>
              <div class="video-info">
                <h3 class="video-title" :title="video.title">
                  {{ video.title }}
                </h3>
                <div class="video-stats">
                  <span class="stat-item">
                    <a-icon type="like" />
                    {{ video.jsonString.likes }} 点赞
                  </span>
                  <span class="stat-item">
                    <a-icon type="message" />
                    {{ video.jsonString.comments }} 评论
                  </span>
                  <span class="stat-item">
                    <a-icon type="share-alt" />
                    {{ video.jsonString.shares }} 分享
                  </span>
                  <span class="stat-item">
                    <a-icon type="check-square" />
                    {{ video.jsonString.collects }} 收藏
                  </span>
                </div>
              </div>
            </div>

            <!-- 右侧评论区 -->
            <div class="right-section">
              <div class="comment-top-title">
                <a-icon type="message" />
                <span
                  >全部评论
                  {{
                    video.comments.length > 0
                      ? " (" + video.comments.length + ")"
                      : ""
                  }}</span
                >
              </div>

              <!-- 评论滚动容器 -->
              <div class="comment-scroll-wrap">
                <div class="comment-list" v-if="video.comments.length > 0">
                  <!-- 已加载的评论列表（追加显示） -->
                  <div
                    v-for="(comment, cIndex) in getLoadedComments(index)"
                    :key="cIndex"
                    class="comment-item"
                  >
                    <a-avatar
                      :size="24"
                      :style="{
                        backgroundColor: getRandomColor(comment.id),
                        color: '#fff',
                      }"
                    >
                      {{ getShortNickname(comment.nickname) }}
                    </a-avatar>
                    <div class="comment-content">
                      <div class="comment-header">
                        <span class="username">{{ comment.nickname }}</span>
                        <span class="time">{{ comment.time }}</span>
                      </div>
                      <div v-if="comment.replyTo" class="chat-reply-quote">
                        <i class="el-icon-chat-line-round quote-icon"></i>
                        <span class="quote-label"
                          >@{{ comment.replyTo }}：</span
                        >
                        <span class="quote-text">{{
                          getReplyContent(comment.replyTo, video.comments)
                        }}</span>
                      </div>
                      <p class="content-text">{{ comment.text }}</p>
                    </div>
                  </div>

                  <!-- 加载更多/无数据提示（动态在评论列表最底部） -->
                  <div class="comment-load-more">
                    <!-- 加载中 -->
                    <a-spin
                      size="small"
                      v-if="getCommentStatus(index).loading"
                    />
                    <!-- 加载更多按钮 -->
                    <a-button
                      v-else-if="hasMoreComments(index)"
                      type="text"
                      size="small"
                      @click="loadMoreComments(index)"
                      class="load-more-btn"
                    >
                      加载更多 <a-icon type="down-circle" theme="twoTone" />
                    </a-button>
                    <!-- 无更多数据提示 -->
                    <span v-else class="no-more-tip"
                      >已加载全部评论，没有更多数据了</span
                    >
                  </div>
                </div>
                <div class="no-comment" v-else>暂无评论~</div>
              </div>
            </div>
          </div>
        </a-card>
      </div>

      <a-pagination
        v-show="Number(total) > 0"
        class="ant-table-pagination"
        show-size-changer
        show-quick-jumper
        :current="queryParam.pageNum"
        :total="total"
        :page-size="queryParam.pageSize"
        :showTotal="(total) => `共 ${total} 条`"
        @showSizeChange="onShowSizeChange"
        @change="changeSize"
      />
    </a-spin>

    <a-back-top />
  </div>
</template>

<script>
import { videoList, getAccount } from "@/api/module/Video";

export default {
  name: "DetailPage",
  data() {
    return {
      queryParam: {
        pageNum: 1,
        pageSize: 10,
        devId: "",
        douyinId: "",
      },
      loading: true,
      videoList: [], // 直接存储接口返回的当前页视频列表
      userInfo: {},
      currentPlayVideoId: null, // 当前播放的视频ID
      total: 0, // 接口返回的总条数
      commentLoadedStatus: {}, // 评论加载状态（按当前页视频索引存储）
    };
  },
  mounted() {
    const { devId, douyinId } = this.$route.query || {};
    this.queryParam = {
      ...this.queryParam,
      devId: devId || "",
      douyinId: douyinId || "",
    };
    this.fetchUserInfo();
    this.getList(); // 初始化加载第一页
  },
  methods: {
    /**
     * 独立获取用户信息
     */
    async fetchUserInfo() {
      const { devId, douyinId } = this.queryParam;
      // 入参校验：无 devId/douyinId 则跳过
      if (!devId || !douyinId) {
        this.userInfo = {};
        return;
      }
      try {
        const userRes = await getAccount({ devId, douyinId });
        // 解析用户信息（保持原有解析逻辑）
        this.userInfo = userRes.account?.jsonString
          ? JSON.parse(userRes.account.jsonString)
          : userRes.account || {};
      } catch (error) {
        console.error("获取用户信息失败：", error);
        this.userInfo = {}; // 失败时置空，避免页面报错
      }
    },
    /**
     * 接口分页获取视频列表
     */
    async getList() {
      this.loading = true;
      try {
        const response = await videoList(this.queryParam); // 接口接收 pageNum/pageSize 返回分页数据

        // 解析当前页视频列表（接口返回的 data 是当前页数据）

        
        this.videoList =
          response.rows?.map((item) => {
            try {
              const content = item.content ? JSON.parse(item.content) : [];
              const jsonString = item.jsonString
                ? JSON.parse(item.jsonString)
                : {};
              return {
                ...item,
                comments: Array.isArray(content) ? content : [],
                jsonString: jsonString || {},
              };
            } catch (e) {
              console.error(`解析content字段失败: ${e.message}`, item);
              return {
                ...item,
                comments: [],
                jsonString: {},
              };
            }
          }) || [];

        // 接口返回的总条数（关键：分页依赖这个值）
        this.total = response.total || 0;

        // 初始化当前页视频的评论加载状态
        this.initCommentLoadedStatus();
      } catch (error) {
        console.error("获取视频列表失败：", error);
        this.videoList = [];
        this.total = 0;
        this.$message.error("获取视频列表失败，请重试");
      } finally {
        this.loading = false;
      }
    },

    // 初始化当前页视频的评论加载状态（默认加载前10条）
    initCommentLoadedStatus() {
      this.commentLoadedStatus = {}; // 清空上一页的状态
      this.videoList.forEach((video, index) => {
        this.$set(this.commentLoadedStatus, index, {
          loadedCount: 10, // 初始加载10条
          pageSize: 10, // 每次加载10条
          total: video.comments.length,
          loading: false,
        });
      });
    },

    // 获取已加载的评论（追加模式）
    getLoadedComments(videoIndex) {
      const video = this.videoList[videoIndex];
      if (!video?.comments || video.comments.length === 0) return [];

      const loadedStatus = this.commentLoadedStatus[videoIndex] || {
        loadedCount: 10,
      };
      return video.comments.slice(0, loadedStatus.loadedCount);
    },

    // 判断是否有更多评论
    hasMoreComments(videoIndex) {
      const loadedStatus = this.commentLoadedStatus[videoIndex] || {
        loadedCount: 0,
        total: 0,
      };
      return loadedStatus.loadedCount < loadedStatus.total;
    },

    // 获取评论加载状态
    getCommentStatus(videoIndex) {
      return this.commentLoadedStatus[videoIndex] || { loading: false };
    },

    // 加载更多评论（追加模式）
    loadMoreComments(videoIndex) {
      const loadedStatus = this.commentLoadedStatus[videoIndex];
      if (loadedStatus?.loading || !this.hasMoreComments(videoIndex)) return;

      // 标记加载中
      this.$set(this.commentLoadedStatus, videoIndex, {
        ...loadedStatus,
        loading: true,
      });

      // 模拟接口加载延迟（若评论是接口分页，此处替换为真实接口请求）
      setTimeout(() => {
        try {
          this.$set(this.commentLoadedStatus, videoIndex, {
            ...loadedStatus,
            loadedCount: loadedStatus.loadedCount + loadedStatus.pageSize,
            loading: false,
          });
        } catch (error) {
          console.error("加载评论失败：", error);
          this.$set(this.commentLoadedStatus, videoIndex, {
            ...loadedStatus,
            loading: false,
          });
        }
      }, 500);
    },

    // 处理视频播放（暂停其他视频）
    handleVideoPlay(playVideoId) {
      if (this.currentPlayVideoId === playVideoId) return;

      // 暂停之前播放的视频
      this.$refs.plyrList?.forEach((videoEl) => {
        if (
          videoEl.dataset.videoId == this.currentPlayVideoId &&
          !videoEl.paused
        ) {
          videoEl.pause();
        }
      });

      this.currentPlayVideoId = playVideoId;
    },

    // 分页条数改变（触发接口请求）
    onShowSizeChange(pageNum, pageSize) {
      this.queryParam.pageSize = pageSize;
      this.queryParam.pageNum = 1; // 重置页码为1
      this.resetVideoPlayStatus(); // 重置播放状态
      this.getList(); // 重新请求接口
      this.scrollToTop();
    },

    // 页码改变（触发接口请求）
    changeSize(pageNum) {
      this.queryParam.pageNum = pageNum;
      this.resetVideoPlayStatus(); // 重置播放状态
      this.getList(); // 重新请求接口
      this.scrollToTop();
    },

    // 重置视频播放状态（分页切换时暂停所有视频）
    resetVideoPlayStatus() {
      this.$refs.plyrList?.forEach((videoEl) => {
        if (!videoEl.paused) videoEl.pause();
      });
      this.currentPlayVideoId = null;
    },

    // 滚动到顶部
    scrollToTop() {
      window.scrollTo({
        top: 0,
        behavior: "smooth",
      });
    },

    // 拼接URL
    getUrl(url) {
      if (!url) return;
      return window.origin + process.env.VUE_APP_BASE_API + url;
    },

    // 随机头像颜色
    getRandomColor(id) {
      const colors = [
        "#4285F4",
        "#3498db",
        "#2980b9",
        "#00bcd4",
        "#0097a7",
        "#607d8b",
        "#EA4335",
        "#e74c3c",
        "#c0392b",
        "#e91e63",
        "#c2185b",
        "#ff6b6b",
        "#FBBC05",
        "#f1c40f",
        "#f39c12",
        "#FF9800",
        "#e67e22",
        "#d35400",
        "#34A853",
        "#2ecc71",
        "#27ae60",
        "#16a085",
        "#1abc9c",
        "#26c6da",
        "#9C27B0",
        "#8e44ad",
        "#6c3483",
        "#9b59b6",
        "#7b1fa2",
        "#ba68c8",
        "#795548",
        "#8d6e63",
        "#5d4037",
        "#95a5a6",
        "#7f8c8d",
        "#bdc3c7",
        "#ff7675",
        "#fd79a8",
        "#fdcb6e",
        "#a29bfe",
        "#00b894",
        "#e17055",
      ];
      return colors[id % colors.length];
    },

    // 获取回复内容
    getReplyContent(replyTo, comments) {
      const targetComment = comments.find((item) => item.nickname === replyTo);
      if (targetComment) {
        return targetComment.text.length > 20
          ? targetComment.text.substring(0, 20) + "..."
          : targetComment.text;
      }
      return "该评论已删除";
    },

    // 截取昵称
    getShortNickname(nickname) {
      if (!nickname) return "匿";
      const maxLen = 2;
      return nickname.length > maxLen
        ? nickname.substring(0, maxLen)
        : nickname;
    },
  },
};
</script>

<style scoped>
.video-list-container {
  padding: 20px;
  max-width: 1600px;
  margin: 0 auto;
  width: 100%;
  box-sizing: border-box;
  overflow: hidden;
}

.page-header-wrapper {
  display: flex;
  flex-direction: column;
  gap: 16px;
  margin-bottom: 24px;
}

.user-info-card {
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.user-info-wrap {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  align-items: center;
}

.user-avatar {
  flex-shrink: 0;
}

.user-base-info {
  flex: 1;
  min-width: 200px;
}

.user-nickname {
  margin: 0 0 8px 0;
  font-size: 22px;
  font-weight: bold;
  color: #2ec6eb;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 100%;
}

.user-gender {
  margin: 0 0 12px 0;
  font-size: 14px;
  color: #666;
  line-height: 1.5;
  white-space: pre-line;
}

.user-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
  font-size: 14px;
  color: #666;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 4px;
}

.user-stats {
  display: flex;
  flex-wrap: wrap;
  gap: 24px;
  padding-left: 16px;
  border-left: 1px solid #eee;
  min-width: 200px;
}

.user-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;
}

.user-label {
  font-size: 12px;
  color: #999;
}

.user-value {
  font-size: 16px;
  font-weight: 600;
  color: #333;
}

.video-card-wrap {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
  width: 100%;
  box-sizing: border-box;
}

.video-card {
  height: 100%;
  box-sizing: border-box;
  overflow: hidden;
}

.card-content {
  display: flex;
  gap: 15px;
  height: 600px;
  padding: 10px 0;
}

.left-section {
  flex: 0 0 48%;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.video-player-wrap {
  width: 100%;
}

.video-player {
  width: 80%;
  height: 550px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.video-info {
  display: flex;
  flex-direction: column;
  gap: 3px;
}

.video-title {
  font-size: 16px;
  color: #333;
  margin: 0;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.video-stats {
  display: flex;
  align-items: center;
  gap: 15px;
  font-size: 11px;
  color: #666;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 4px;
}

.right-section {
  flex: 0 0 52%;
  display: flex;
  flex-direction: column;
  height: 100%;
  border-left: 1px solid #eee;
  padding-left: 15px;
}

.comment-header {
  font-size: 13px;
  margin-bottom: 8px;
  display: flex;
  justify-content: space-between;
  gap: 8px;
  width: 100%;
}

.comment-top-title {
  font-size: 13px;
  margin-bottom: 8px;
  display: flex;
  align-items: center;
  gap: 8px;
  width: 100%;
}

/* 评论滚动容器 */
.comment-scroll-wrap {
  flex: 1;
  height: 220px;
  overflow-y: auto;
  padding-right: 5px;
  scrollbar-width: thin;
  scrollbar-color: #ddd #f5f5f5;
}

.comment-scroll-wrap::-webkit-scrollbar {
  width: 6px;
}

.comment-scroll-wrap::-webkit-scrollbar-thumb {
  background-color: #ddd;
  border-radius: 3px;
}

.comment-scroll-wrap::-webkit-scrollbar-track {
  background-color: #f5f5f5;
}

/* 评论列表（包含加载更多/提示） */
.comment-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.comment-item {
  display: flex;
  gap: 8px;
  padding: 4px 0;
  border-bottom: 1px solid #f5f5f5;
  padding-bottom: 8px;
}

.comment-content {
  flex: 1;
}

.username {
  font-weight: 500;
  color: #333;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: calc(100% - 60px);
}

.time {
  font-size: 11px;
  color: #999;
}

.content-text {
  font-size: 13px;
  color: #666;
  margin: 0 0 6px 0;
  line-height: 1.4;
  word-wrap: break-word;
  word-break: break-all;
  max-height: 60px;
  overflow: hidden;
}

.no-comment {
  text-align: center;
  padding: 30px 0;
  color: #999;
  font-size: 13px;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

/* 加载更多/无数据提示样式（动态在评论最底部） */
.comment-load-more {
  text-align: center;
  padding: 8px 0;
  margin-top: 4px;
}

.load-more-btn {
  color: #1890ff;
  font-size: 12px;
}

.load-more-btn:hover {
  color: #40a9ff;
  background: #f5f8ff;
}

.no-more-tip {
  font-size: 12px;
  color: #999;
  line-height: 1.5;
}

/* 回复引用样式 */
.chat-reply-quote {
  display: flex;
  align-items: flex-start;
  padding: 4px 8px;
  background-color: #f0f2f5;
  border-radius: 4px;
  font-size: 12px;
  color: #666;
  margin: 4px 0;
  border-left: 2px solid #4285f4;
  width: 100%;
  box-sizing: border-box;
  max-height: 40px;
  overflow: hidden;
}

.quote-icon {
  font-size: 12px;
  color: #4285f4;
  margin-right: 4px;
  margin-top: 1px;
}

.quote-label {
  font-weight: 600;
  color: #999;
  margin-right: 2px;
  font-size: 12px;
}

.quote-text {
  font-size: 12px;
  color: #666;
  word-wrap: break-word;
  word-break: break-all;
  flex: 1;
}

.ant-table-pagination {
  margin-top: 20px;
  text-align: center;
}

/* 移动端适配 */
@media (max-width: 1200px) {
  .video-card-wrap {
    grid-template-columns: 1fr;
  }
  .card-content {
    flex-direction: column;
  }
  .left-section,
  .right-section {
    flex: 1;
    width: 100%;
  }
  .right-section {
    border-left: none;
    border-top: 1px solid #eee;
    padding-left: 0;
    padding-top: 15px;
    margin-top: 10px;
  }
  .comment-scroll-wrap {
    height: 200px;
  }
}

@media (max-width: 480px) {
  .video-player {
    min-width: 300px;
  }
}
</style>