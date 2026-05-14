<template>
  <a-modal
    title="查看视频"
    :visible="open"
    @cancel="cancel"
    width="30%"
    :footer="null"
    destroyOnClose
  >
    <!-- 视频容器，设置为100%宽高，让视频自适应 -->
    <div class="video-modal-container">
      <video
        ref="plyr"
        controls
        class="video-player"
        playsinline
        webkit-playsinline
        autoplay
      >
        <source :src="getVideoUrl()" type="video/mp4" />
      </video>
    </div>
  </a-modal>
</template>

<script>
export default {
  name: "PlayVideo",
  props: {},
  components: {},
  data() {
    return {
      open: false,
      videoPath: "",
    };
  },
  filters: {},
  created() {},
  computed: {},
  watch: {},
  mounted() {},
  methods: {
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
      // 可选：关闭时暂停视频播放
      const video = this.$refs.plyr;
      if (video) video.pause();
    },
    // 拼接视频完整地址
    getVideoUrl() {
      return window.origin + process.env.VUE_APP_BASE_API + this.videoPath;
    },
    reset() {
      this.videoPath = "";
    },

    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      this.open = true;
      this.videoPath = row.videoPath;
    },
  },
};
</script>


<style scoped>
/* 模态框内容容器，去除默认padding并占满高度 */
.video-modal-container {
  width: 100%;
  height: calc(100vh - 200px); /* 适配模态框头部和底部，可根据实际调整 */
  padding: 0;
}

/* 视频播放器占满容器 */
.video-player {
  width: 100%;
  height: 100%;
  object-fit: contain; /* 保持视频比例，避免拉伸，也可设为cover（铺满但裁剪） */
}

/* 适配小屏幕，调整模态框内容高度 */
@media (max-width: 768px) {
  .video-modal-container {
    height: calc(100vh - 150px);
  }
}
</style>
