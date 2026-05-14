<template>
  <a-modal
  :title="formTitle"
    :visible="open"
    :confirm-loading="submitLoading"
    @ok="submitForm"
    @cancel="cancel"
  >

    <a-form-model ref="form" :model="form" :rules="rules">
      <!-- <a-form-model-item label="分配用户" prop="users">
          <a-select placeholder="请选择分配用户" v-model="form.users" mode="multiple" style="width: 100%" allow-clear>
            <a-select-option v-for="(d, index) in userData" :key="index" :value="d.userName + '_' + d.userId">{{
              d.userName
            }}</a-select-option>
          </a-select>
        </a-form-model-item>
        <a-form-model-item label="设备" prop="devId">
          <a-select placeholder="请选择设备分组" v-model="form.devId" mode="multiple" style="width: 100%" allow-clear>
            <a-select-option v-for="(d, index) in deviceData" :key="index" :value="d.deviceName + '_' + d.id">{{
              d.deviceName + '_' + d.id
            }}</a-select-option>
          </a-select>
        </a-form-model-item> -->
      <a-form-model-item label="策略名称" prop="strategyName">
        <a-input
          v-model="form.strategyName"
          placeholder="请输入策略名称"
          allow-clear
        />
      </a-form-model-item>
      <a-form-model-item label="策略方法" prop="scriptPath">
        <a-textarea
          placeholder="请输入策略方法"
          v-model="form.scriptPath"
          :auto-size="{ minRows: 4, maxRows: 13 }"
        />
      </a-form-model-item>
      <a-form-model-item label="策略描述" prop="description">
        <a-textarea
          placeholder="请输入策略描述"
          v-model="form.description"
          :auto-size="{ minRows: 4, maxRows: 13 }"
        />
      </a-form-model-item>
    </a-form-model>
  </a-modal>
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
  name: "AddTask",
  props: {},
  components: {},
  data() {
    return {
      submitLoading: false,
      form: {
        strategyName: "",
        scriptPath: "",
        description: "",
        id: null,
      },
      formTitle: "",
      // 1增加,2修改
      open: false,
      rules: {
        strategyName: [
          { required: true, message: "请输入策略名称", trigger: "blur" },
        ],
        scriptPath: [
          { required: true, message: "请输入策略方法", trigger: "blur" },
        ],
        description: [
          { required: true, message: "请输入策略描述", trigger: "blur" },
        ],
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
    handleUpdate(record) {
      this.reset();
      this.formTitle = '修改任务';
      this.form.id = record.id;
      this.form.strategyName = record.strategyName;
      this.form.scriptPath = record.scriptPath;
      this.form.description = record.description;

      this.open = true;
    },

    onClose() {
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
    /** 新增按钮操作 */
    handleAdd(row) {
      this.formTitle = "添加任务";

      this.reset();
      this.open = true;
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.submitLoading = true;
          if (this.form.id !== undefined && this.form.id !== null) {
            updateTask(this.form)
              .then((response) => {
                this.$message.success("修改成功", 3);
                this.open = false;
                this.$emit("ok");
              })
              .finally(() => {
                this.submitLoading = false;
              });
          } else {
            addTask(this.form)
              .then((response) => {
                this.$message.success("添加完成", 3);
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
