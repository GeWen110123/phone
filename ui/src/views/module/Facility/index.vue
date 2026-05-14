<template>
  <a-card :bordered="false">
    <!-- 条件搜索 -->
    <!-- <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="48">
          <a-col :md="6" :sm="24">
            <a-form-item label="用户名" prop="userName">
              <a-input v-model.trim="queryParam.userName" placeholder="请输入用户名" allow-clear />
            </a-form-item>
          </a-col>

          <a-col :md="6" :sm="24">
            <a-form-item label="类型" prop="type">
              <a-input v-model.trim="queryParam.type" placeholder="请输入类型" allow-clear />
            </a-form-item>
          </a-col>

          <a-col :md="8" :sm="24">
            <span class="table-page-search-submitButtons">
              <a-button type="primary" @click="handleQuery"><a-icon type="search" />查询</a-button>
              <a-button style="margin-left: 8px" @click="resetQuery"><a-icon type="redo" />重置</a-button>

            </span>
          </a-col>
        </a-row>
      </a-form>
    </div> -->
    <!-- 操作 -->
    <div class="table-operations">
      <a-button type="primary" :disabled="selectedRowKeys.length == 0" @click="changeGroup">
        <a-icon type="plus" />设备分组
      </a-button>

      <!-- <a-button type="danger" :disabled="multiple" @click="handleDelete" v-hasPermi="['module:inquireall:remove']">
        <a-icon type="delete" />删除
      </a-button>
      <a-button type="primary" @click="handleExport" v-hasPermi="['module:inquireall:export']">
        <a-icon type="download" />导出
      </a-button> -->
      <table-setting :style="{ float: 'right' }" :table-size.sync="tableSize" v-model="columns"
        :refresh-loading="loading" @refresh="getList" />
    </div>
    <!-- 增加修改 -->
    <create-form ref="createForm" @ok="getList" />
    <!-- 数据展示 -->
    <a-table :loading="loading" :size="tableSize" rowKey="id" :columns="columns" :data-source="list"
      :row-selection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }" :pagination="false">
      <span slot="groups" slot-scope="text, record">
        <a-tag color="blue" v-for="(d, index) in (record.groups ? record.groups.split(',') : [])" :key="index"
          style="margin: 2px;">
          {{ d }}
        </a-tag>
      </span>
      <a-form-model-item label="分配用户" prop="users">
        <a-select placeholder="请选择分配用户" v-model="editForm.users" mode="multiple" style="width: 100%" allow-clear>
          <a-select-option v-for="(d, index) in userData" :key="index" :value="d.userName + '_' + d.userId">{{
            d.userName
          }}</a-select-option>
        </a-select>
      </a-form-model-item>

      <span slot="status" slot-scope="text, record">
        <a-tag v-if="record.status == 0">未连接</a-tag>
        <a-tag color="#87d068" v-else-if="record.status == 1">已连接</a-tag>
      </span>

      <span slot="operation" slot-scope="text, record">
        <div style="margin-bottom: 6px;">
          <a @click="handleUpdate(record, undefined)">
            <a-icon type="edit" />修改
          </a>

          <a-divider type="vertical" />
          <a @click="openApp(record)">
            <a-icon type="database" />应用列表
          </a>
          <a-divider type="vertical" />
          <a @click="handleUpdate(record)" disabled>
            <a-icon type="phone" />手机管理
          </a>
          <a-divider type="vertical" />
          <a @click="openLog(record)">
            <a-icon type="pull-request" />活动日志
          </a>
          <a-divider type="vertical" />
          <a @click="handleUpdate(record)" disabled>
            <a-icon type="wifi" />设备镜像
          </a>
        </div>
        <div>
          <a @click="searchDy(record)">
            <a-icon type="search" />抖音搜索
          </a>
          <a-divider type="vertical" />
          <a @click="$refs.viewData.open(record.deviceCode)">
            <a-icon type="search" />详细数据
          </a>
        </div>

      </span>
    </a-table>
    <!-- 分页 -->
    <a-pagination class="ant-table-pagination" show-size-changer show-quick-jumper :current="queryParam.pageNum"
      :total="total" :page-size="queryParam.pageSize" :showTotal="total => `共 ${total} 条`"
      @showSizeChange="onShowSizeChange" @change="changeSize" />



    <a-modal title="批量设置分组" :visible="groupVisableShow" :confirm-loading="submitLoading" @ok="submitForm"
      @cancel="cancel">
      <a-form-model ref="form" :model="form" :rules="rules">
        <a-form-model-item label="设备分组" prop="groupIds">
          <a-select placeholder="请选择设备分组" v-model="form.groupIds" mode="multiple" style="width: 100%" allow-clear>
            <a-select-option v-for="(d, index) in groupOption" :key="index" :value="d.id">{{
              d.groupName
            }}</a-select-option>
          </a-select>
        </a-form-model-item>
      </a-form-model>
    </a-modal>

    <a-modal title="修改设备信息" width="40%" :visible="editVisible" :body-style="{ paddingBottom: '80px' }"
      @cancel="onClose">
      <a-form-model ref="editForm" :model="editForm" :rules="editRules">

        <a-form-model-item label="分配用户" prop="users">
          <a-select placeholder="请选择分配用户" v-model="editForm.users" mode="multiple" style="width: 100%" allow-clear>
            <a-select-option v-for="(d, index) in userData" :key="index" :value="d.userName + '_' + d.userId">{{
              d.userName
            }}</a-select-option>
          </a-select>
        </a-form-model-item>
        <a-form-model-item label="设备名" prop="deviceName">
          <a-input v-model="editForm.deviceName" placeholder="请输入设备名称" allow-clear />
        </a-form-model-item>

        <a-form-model-item label="设备分组">
          <a-select placeholder="请选择" v-model="editForm.groupIds" mode="multiple" style="width: 100%" allow-clear>
            <a-select-option v-for="(d, index) in groupOption" :key="index" :value="d.id">{{
              d.groupName
            }}</a-select-option>
          </a-select>
        </a-form-model-item>

      </a-form-model>
      <div :style="{
        position: 'absolute',
        right: 0,
        bottom: 0,
        width: '100%',
        borderTop: '1px solid #e9e9e9',
        padding: '10px 16px',
        background: '#fff',
        textAlign: 'right',
        zIndex: 1,
      }">
        <a-button :style="{ marginRight: '8px' }" @click="onClose">
          Cancel
        </a-button>
        <a-button type="primary" :loading="editLoading" @click="SubmitEditForm">
          Submit
        </a-button>
      </div>
    </a-modal>



    <a-modal title="设备活动日志" width="70%" :visible="logVisible" :body-style="{ paddingBottom: '80px' }"
      @cancel="onLogClose" :footer="null">

      <a-empty v-if="logData.length == 0" :description="false" />
      <a-timeline>
        <a-timeline-item v-for="(item, i) in logData" :key="i">
          <a-icon slot="dot" type="clock-circle-o" style="font-size: 16px;" />
          <p style="font-size: 15px;color: #4465ED;">{{ item.createTime }}</p>
          <p style="font-size: 15px;">{{ item.actionDetail }}</p>
        </a-timeline-item>
      </a-timeline>

    </a-modal>

    <a-modal title="应用列表" width="70%" :visible="appVisible" :body-style="{ paddingBottom: '80px' }" @cancel="onAppClose"
      :footer="null">
      <a-empty v-if="appData.length == 0" :description="false" />

      <div class="card-container">
        <a-card hoverable style="width: 180px" v-for="(item, i) in appData" :key="i">
          <img v-if="item.appName == '油管'" slot="cover" alt="example" src="../../../assets/images/X.png" />
          <img v-else-if="item.appName == '抖音'" slot="cover" alt="example" src="../../../assets/images/dy.png" />
          <img v-else-if="item.appName == '小红书'" slot="cover" alt="example" src="../../../assets/images/xhs.png" />
          <a-card-meta :title="item.appName" style="display: flex;align-items: center;" :description="item.installTime">
            <a-avatar v-if="item.status == 1" slot="avatar" :size="32"
              style="backgroundColor:#87d068;verticalAlign: 'middle'" icon="check" />
            <a-avatar v-else slot="avatar" :size="32" style="backgroundColor:#F56C6C;verticalAlign: 'middle'"
              icon="exclamation" />
          </a-card-meta>
        </a-card>
      </div>

    </a-modal>


    <a-modal title="抖音用户搜索" width="40%" :visible="dySearchVisible" :body-style="{ paddingBottom: '80px' }"
      @cancel="onDyClose">
      <a-form-model ref="dySearchForm" :model="dyForm" :rules="dyRules">
        <a-form-model-item label="抖音用户" prop="douyinId">
          <a-input v-model="dyForm.douyinId" placeholder="只能搜索一个抖音号，内容为精准匹配" allow-clear />
        </a-form-model-item>
      </a-form-model>
      <div :style="{
        position: 'absolute',
        right: 0,
        bottom: 0,
        width: '100%',
        borderTop: '1px solid #e9e9e9',
        padding: '10px 16px',
        background: '#fff',
        textAlign: 'right',
        zIndex: 1,
      }">
        <a-button :style="{ marginRight: '8px' }" @click="onDyClose">
          Cancel
        </a-button>
        <a-button type="primary" :loading="dyLoading" @click="SubmitdyForm">
          Submit
        </a-button>
      </div>
    </a-modal>



    <ViewData ref="viewData"></ViewData>

  </a-card>
</template>

<script>
import { deviceList, updateDevice, getDeviceGroup, deviceLog, deviceApp, listUser } from '@/api/module/Facility'
import CreateForm from './modules/CreateForm'
import ViewData from './modules/viewData'
import { tableMixin } from '@/store/table-mixin'
import { mapGetters } from 'vuex'

import { getCrawl } from '@/api/module/UserSearch'

export default {
  name: 'Facility',
  components: {
    CreateForm,
    ViewData
  },
  mixins: [tableMixin],
  data() {
    return {
      dySearchVisible: false,
      editVisible: false,
      logVisible: false,
      appVisible: false,
      appData: [],
      dyLoading: false,
      logData: '',
      editForm: {
        deviceName: null,
        groupIds: undefined,
        users: [],

      },
      dyForm: {
        devId: '',
        douyinId: '',
      },

      dyRules: {
        douyinId: [{ required: true, message: '请输入抖音号，内容为精准匹配', trigger: 'blur' }],
      },
      editLoading: false,
      editRules: {
        deviceName: [{ required: true, message: '请输入设备名', trigger: 'blur' }],
        users: [{ required: true, message: '请选择分配用户', trigger: 'change' }],
      },
      id: "",
      groupVisableShow: false,
      submitLoading: false,
      groupOption: [],
      form: {
        groupIds: undefined,
      },
      rules: {
        groupIds: [{ required: true, message: '请选择设备分组', trigger: 'change' }],
      },
      list: [],
      selectedRowKeys: [],
      selectedRows: [],
      userData: [],
      multiple: true,
      ids: [],
      loading: false,
      total: 0,
      // 查询参数
      queryParam: {

        time: null,
        deptName: null,
        userName: null,
        content: null,
        type: null,
        pageNum: 1,
        pageSize: 100
      },
      columns: [
        {
          title: 'ID',
          dataIndex: 'id',
          ellipsis: true,
          width: 90,
          align: 'center'
        },
        {
          title: '设备码',
          dataIndex: 'deviceCode',
          ellipsis: true,
          width: 160,
          align: 'center'
        },
        {
          title: '设备名',
          dataIndex: 'deviceName',
          width: 130,
          ellipsis: true,
          align: 'center'
        },

        {
          title: '手机型号',
          dataIndex: 'model',
          ellipsis: true,
          align: 'center'
        },
        {
          title: '系统版本',
          dataIndex: 'systemVersion',
          ellipsis: true,
          align: 'center'
        },
        {
          title: '内核版本',
          dataIndex: 'kernelVersion',
          ellipsis: true,
          align: 'center'
        },
        {
          title: '硬盘占用',
          dataIndex: 'storageUsage',
          ellipsis: true,
          align: 'center'
        },
        {
          title: '分配用户',
          dataIndex: 'users',
          ellipsis: true,
          width: 100,

          scopedSlots: { customRender: 'users' },
          align: 'left'
        },
        {
          title: '状态',
          dataIndex: 'status',
          width: 100,

          scopedSlots: { customRender: 'status' },
          ellipsis: true,
          align: 'center'
        },
        {
          title: '设备分组',
          width: 200,
          dataIndex: 'groups',
          scopedSlots: { customRender: 'groups' },
          align: 'left'
        },


        {
          title: '操作',
          dataIndex: 'operation',
          width: 400,
          scopedSlots: { customRender: 'operation' },
          align: 'left'
        }
      ]
    }
  },
  filters: {
  },
  created() {
    this.getList()



  },


  methods: {
    /** 查询搜索日志列表 */
    getList() {
      getDeviceGroup().then(response => {
        this.groupOption = response.data
      })
      listUser().then(response => {
        this.userData = response.rows
      })

      this.loading = true
      deviceList().then(response => {
        this.list = response.rows
        this.total = response.total
        this.loading = false
      })
    },

    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParam.pageNum = 1
      this.getList()
    },

    /** 重置按钮操作 */
    resetQuery() {
      this.queryParam = {
        user: undefined,
        time: undefined,
        deptName: undefined,
        userName: undefined,
        content: undefined,
        type: undefined,
        pageNum: 1,
        pageSize: 10
      }
      this.handleQuery()
    },

    changeGroup() {
      this.groupVisableShow = true;
    },
    onShowSizeChange(current, pageSize) {
      this.queryParam.pageSize = pageSize
      this.getList()
    },
    changeSize(current, pageSize) {
      this.queryParam.pageNum = current
      this.queryParam.pageSize = pageSize
      this.getList()
    },
    onSelectChange(selectedRowKeys, selectedRows) {
      this.selectedRowKeys = selectedRowKeys
      this.selectedRows = selectedRows
      this.ids = this.selectedRows.map(item => item.id)
      this.multiple = !selectedRowKeys.length
    },


    submitForm() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          let data = {
            ids: this.selectedRowKeys,
            groupIds: this.form.groupIds,

          }
          updateDevice(data)
            .then((response) => {
              this.form.groupIds = undefined;
              this.$message.success('已修改完成', 3)
              this.getList()
              this.groupVisableShow = false
            })
            .finally(() => {
              this.groupVisableShow = false
            })
        } else {
          return false
        }
      })

    },



    cancel() {
      this.form.groupIds = undefined;
      this.groupVisableShow = false;

    },
    /** 删除按钮操作 */
    handleDelete(row) {
      var that = this
      const ids = row.id || this.ids
      this.$confirm({
        title: '确认删除所选中数据?',
        content: '当前选中编号为' + ids + '的数据',
        onOk() {
          return delInquireall(ids)
            .then(() => {
              that.onSelectChange([], [])
              that.getList()
              that.$message.success(
                '删除成功',
                3
              )
            })
        },
        onCancel() { }
      })
    },
    /** 导出按钮操作 */
    handleExport() {
      var that = this
      this.$confirm({
        title: '是否确认导出?',
        content: '此操作将导出当前条件下所有数据而非选中数据',
        onOk() {
          that.download('module/inquireall/export', {
            ...that.queryParam
          }, `inquireall_${new Date().getTime()}.xlsx`)
        },
        onCancel() { }
      })
    },

    handleUpdate(record) {
      this.id = record.id
      this.editVisible = true;

      this.editForm.deviceName = record.deviceName;
      this.editForm.groupIds = record.groupIds ? record.groupIds : undefined;

    },
    searchDy(record) {
      this.dyForm.devId = record.deviceCode;
      this.dySearchVisible = true;

    },

    openLog(record) {

      let data = {
        deviceId: record.id,
      }
      deviceLog(data).then(response => {
        this.logData = response.data
        this.logVisible = true;
      })
    },

    openApp(record) {
      let data = {
        deviceId: record.id,
      }
      deviceApp(data).then(response => {
        this.appData = response.rows
        this.appVisible = true;
      })
    },
    onAppClose() {
      this.appVisible = false;
    },
    onLogClose() {
      this.logVisible = false;
      this.logData = []
    },

    onClose() {
      this.editVisible = false;
      this.id = null;
      this.editForm = {
        deviceName: null,
        groupIds: undefined,
        users: [],
      }

    },
    onDyClose() {
      this.dySearchVisible = false;
      this.dyForm = {
        devId: null,
        douyinId: ''
      }
    },

    SubmitdyForm() {
      this.$refs.dySearchForm.validate((valid) => {
        if (valid) {
          getCrawl(this.dyForm)
            .then((response) => {
              this.$message.success(response.msg, 3)
              this.getList()
              this.onDyClose()
            })
            .finally(() => {
                           this.onDyClose()
            })
        } else {
          return false
        }
      })

    },
    SubmitEditForm() {
      this.$refs.editForm.validate((valid) => {
        if (valid) {
          this.editLoading = true
          let data = {
            ids: [this.id],
            deviceName: this.editForm.deviceName,
            groupIds: this.editForm.groupIds,
            users: this.editForm.users.join(','),

          }
          updateDevice(data)
            .then((response) => {
              this.$message.success('已修改完成', 3)
              this.getList()
              this.editVisible = false
            })
            .finally(() => {
              this.editLoading = false
            })
        } else {
          return false
        }
      })
    },
  }
}
</script>

<style lang="less">
.title-box {
  transition: margin-top 1s ease;
  /* 对 margin-top 设置过渡动画 */
  text-align: center;
  animation: 1s fadeIn 1s;
  position: relative;

}

.btn_box {
  a {
    margin: 0 4px;
  }
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 1.3s ease;
}

.fade-enter,
.fade-leave-to {
  opacity: 0;
  /* 图片消失时变为透明 */
}

.card-container {
  display: flex;
  flex-direction: row;
  /* 垂直排列 */
  gap: 20px;
  /* 设置卡片之间的间隔 */
  justify-content: flex-start;
  /* 确保从顶部开始排列 */
  align-items: center;
  /* 使卡片居中对齐（可选） */
  padding: 20px;

  .ant-card-meta-title {
    font-weight: bold;
    font-size: 17px;
    color: #66B1FF;
  }
}
</style>
