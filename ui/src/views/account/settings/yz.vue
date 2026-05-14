<template>
  <div class="app-container">
    <h2> 双因子验证：</h2>

    <a-button type="primary" size="small" @click="getTcode">重置并激活</a-button>
    <a-button type="primary" v-if="statusText == true" style="margin-left: 6px;" size="small"
      @click="resetUsertcode">关闭</a-button>
    <!-- <a-tag :color="tcodeStatus">{{ statusText }}</a-tag> -->
    <a-modal title="提示" :visible="dialogVisible" @cancel="handleClose" @ok="handleClose" :width="'22%'"
      :body-style="{ height: '30%' }" footer=''>
      <img :src="codeUrl" class="login-code-img" />
      <!-- <a-form :model="user" ref="form" :rules="rules">
        <a-form-item label="动态口令" prop="tcode">
          <a-input v-model="user.tcode" placeholder="请输入扫码后OTP软件的生成动态code用以验证并绑定，注意倒计时有效性" />
          <a-button type="primary" @click="Tcodevaild">验证</a-button>
        </a-form-item>
      </a-form> -->
      <a-form-model ref="form" :model="form" :rules="rules">
        <a-form-model-item label="动态口令" prop="tcode">
          <a-input v-model.trim="form.tcode" placeholder="请输入扫码后OTP软件的生成动态code用以验证并绑定，注意倒计时有效性" />
        </a-form-model-item>
      </a-form-model>
      <div style="text-align: right;">
        <a-button type="primary" @click="Tcodevaild">验证</a-button>

      </div>

    </a-modal>
  </div>
</template>

<script>
import request from '@/utils/request';
import { vaildTcode } from "@/api/system/user";
import { resetUserTcode } from "@/api/system/user";
import { tcodeStatus } from "@/api/system/user";

export default {
  data() {
    return {
      statusText: "GGGGGG",
      tcodeStatus: "#E6A23C",
      codeUrl: '',
      dialogVisible: false,
      form: {
        tcode: undefined,
      },

      rules: {
        tcode: [
          { required: true, message: "不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.gettcodeStatus();
  },
  methods: {
    getTcode() {
      this.dialogVisible = true;
      this.form.tcode = undefined;
      return request({
        url: '/system/user/profile/tcodesetup',
        method: 'get',
      }).then(res => {
        console.log(res.img);
        this.codeUrl = "data:image/gif;base64," + res.img;
      });
    },

    Tcodevaild() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          vaildTcode(this.form.tcode).then(response => {
            this.$message.success("绑定成功!");
            this.gettcodeStatus()
            this.dialogVisible = false;
          });
        }
      });
    },

    resetUsertcode() {
      resetUserTcode().then(response => {
        this.$message.success("关闭成功!");
        this.gettcodeStatus()

      });
    },

    gettcodeStatus() {
      tcodeStatus().then(response => {
        this.statusText = response.tcodeStatus;
      });
    },

    handleClose() {
      this.dialogVisible = false;
      this.codeUrl = "";
    }
  }
};
</script>

<style scoped>
.login-code-img {
  width: 100%;
  height: 100%;
}
</style>
