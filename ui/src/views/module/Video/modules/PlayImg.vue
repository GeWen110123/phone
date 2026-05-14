<template>
  <a-modal
    title="查看图片"
    :visible="open"
    @cancel="cancel"
    width="30%"
    :footer="null"
    destroyOnClose
  >
    <!-- 图片容器，适配图片展示 -->
    <div class="img-modal-container">
      <img :src="getImgUrl()" class="img-content" alt="展示图片" />
    </div>
  </a-modal>
</template>

<script>
export default {
  name: "PlayImg",
  props: {},
  components: {},
  data() {
    return {
      open: false,
      imgPath: "", // 改为图片路径变量
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
    },
    // 拼接图片完整地址
    getImgUrl() {
      return window.origin + process.env.VUE_APP_BASE_API + this.imgPath;
    },
    reset() {
      this.imgPath = ""; // 重置图片路径
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      this.open = true;
      this.imgPath = row.imagePath; // 改为图片路径（根据实际字段名调整）
    },
  },
};
</script>

<style scoped>
/* 模态框内容容器，适配图片展示 */
.img-modal-container {
  width: 100%;
  height: calc(100vh - 200px);
  padding: 0;
  display: flex;
  justify-content: center;
  align-items: center;
  overflow: auto; /* 处理图片过大时的滚动 */
}

/* 图片样式，保持比例并适配容器 */
.img-content {
  max-width: 100%;
  max-height: 100%;
  object-fit: contain; /* 保持图片比例，不拉伸 */
}

/* 适配小屏幕，调整容器高度 */
@media (max-width: 768px) {
  .img-modal-container {
    height: calc(100vh - 150px);
  }
}
</style>