<template>
  <a-drawer
    title="执行任务"
    :width="550"
    :visible="open"
    :body-style="{ paddingBottom: '80px' }"
    @close="onClose"
  >
    <a-form-model ref="form" :model="form" :rules="rules">
      <a-form-model-item label="设备" prop="devId">
        <a-select
          placeholder="请选择设备"
          v-model="form.devId"
          mode="multiple"
          style="width: 100%"
          allow-clear
        >
          <a-select-option
            v-for="(d, index) in deviceData"
            :key="index"
            :value="d.deviceName + '_' + d.id"
            >{{ d.deviceName + "_" + d.id }}</a-select-option
          >
        </a-select>
      </a-form-model-item>
    </a-form-model>
    <div
      :style="{
        position: 'absolute',
        right: 0,
        bottom: 0,
        width: '100%',
        borderTop: '1px solid #e9e9e9',
        padding: '10px 16px',
        background: '#fff',
        textAlign: 'right',
        zIndex: 1,
      }"
    >
      <a-button :style="{ marginRight: '8px' }" @click="onClose">
        Cancel
      </a-button>
      <a-button type="primary" :loading="submitLoading" @click="submitForm">
        Submit
      </a-button>
    </div>
  </a-drawer>
</template>

<script>
import {
  updateTask,
  task,
  goToTask,
  addTask,
  deviceList,
  groupAll,
} from "@/api/module/Strategy";
export default {
  name: "runTask",
  props: {},
  components: {},
  data() {
    return {
      submitLoading: false,
      form: {
        devId: [],
      },
      deviceData: [],
      formTitle: "",
      // 1增加,2修改
      open: false,
      rules: {
        devId: [{ required: true, message: "请选择设备", trigger: "change" }],
      },
    };
  },
  filters: {},
  created() {},
  computed: {},
  watch: {},
  mounted() {},
  methods: {
    /** 修改按钮操作 */
    handleUpdate(record, deviceData) {
      this.deviceData = deviceData
      this.reset();
      this.formTitle = "执行任务";
      this.form.id = record.id;

      this.open = true;
    },

    onClose() {
      this.form.devId = [];
      this.open = false;
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        strategyName: "",
        scriptPath: "",
        description: "",
      };
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.submitLoading = true;
          if (this.form.id !== undefined && this.form.id !== null) {
            let data = {
              id: this.form.id,
              devId: this.form.devId.join(","),
            };
            goToTask(data)
              .then((response) => {
                this.$message.success("执行成功", 3);
                this.open = false;
                this.$emit("ok");
              })
              .finally(() => {
                this.submitLoading = false;
              });
          }
        } else {
          return false;
        }
      });
    },
  },
};
</script>
