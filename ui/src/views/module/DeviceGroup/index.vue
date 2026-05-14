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
      <a-button type="primary" @click="changeGroup">
        <a-icon type="plus" />新增设备分组
      </a-button>

      <!-- <a-button type="danger" :disabled="multiple" @click="handleDelete" v-hasPermi="['module:inquireall:remove']">
        <a-icon type="delete" />删除
      </a-button>
      <a-button type="primary" @click="handleExport" v-hasPermi="['module:inquireall:export']">
        <a-icon type="download" />导出
      </a-button> -->
      <table-setting
        :style="{ float: 'right' }"
        :table-size.sync="tableSize"
        v-model="columns"
        :refresh-loading="loading"
        @refresh="getList"
      />
    </div>
    <!-- 增加修改 -->
    <!-- <create-form ref="createForm" @ok="getList" /> -->
    <!-- 数据展示 -->

    <!-- :row-selection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }" -->
    <a-table
      :loading="loading"
      :size="tableSize"
      rowKey="id"
      :columns="columns"
      :data-source="list"
      :pagination="false"
    >
      <span slot="deviceName" slot-scope="text, record">
        <editable-cell
          :text="text"
          @change="onCellChange(record.key, 'name', $event)"
        />
      </span>

      <span slot="devId" slot-scope="text, record">
        <a-tag
          color="blue"
          v-for="(d, index) in record.devId ? record.devId.split(',') : []"
          :key="index"
          style="margin: 2px"
        >
          {{ d }}
        </a-tag>
      </span>
      <span slot="users" slot-scope="text, record">
        <a-tag
          color="green"
          v-for="(d, index) in record.users ? record.users.split(',') : []"
          :key="index"
          style="margin: 2px"
        >
          {{ d }}
        </a-tag>
      </span>

      <span slot="status" slot-scope="text, record">
        <a-tag v-if="record.status == 0">未连接</a-tag>
        <a-tag color="#87d068" v-else-if="record.status == 1">已连接</a-tag>
      </span>

      <span slot="operation" slot-scope="text, record">
        <a @click="handleUpdate(record, undefined)">
          <a-icon type="edit" />修改
        </a>
        <!-- <a-divider type="vertical" /> -->
        <!-- <a-divider type="vertical" v-hasPermi="['module:inquireall:remove']" />
        <a @click="handleDelete(record)" v-hasPermi="['module:inquireall:remove']">
          <a-icon type="delete" />删除
        </a>  -->
      </span>
    </a-table>
    <!-- 分页 -->
    <!-- <a-pagination class="ant-table-pagination" show-size-changer show-quick-jumper :current="queryParam.pageNum"
      :total="total" :page-size="queryParam.pageSize" :showTotal="total => `共 ${total} 条`"
      @showSizeChange="onShowSizeChange" @change="changeSize" /> -->

    <a-modal
      title="新增设置分组"
      :visible="groupVisableShow"
      :confirm-loading="submitLoading"
      @ok="submitForm"
      @cancel="cancel"
    >
      <a-form-model ref="form" :model="form" :rules="rules">
        <a-form-model-item label="分配用户" prop="users">
          <a-select
            placeholder="请选择分配用户"
            v-model="form.users"
            mode="multiple"
            style="width: 100%"
            allow-clear
          >
            <a-select-option
              v-for="(d, index) in userData"
              :key="index"
              :value="d.userName + '_' + d.userId"
              >{{ d.userName }}</a-select-option
            >
          </a-select>
        </a-form-model-item>
        <a-form-model-item label="设备" prop="devId">
          <a-select
            placeholder="请选择设备分组"
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
        <a-form-model-item label="分组名" prop="groupName">
          <a-input
            v-model="form.groupName"
            placeholder="请输入分组名"
            allow-clear
          />
        </a-form-model-item>
        <a-form-model-item label="备注">
          <a-input
            v-model="form.remark"
            placeholder="请输入备注信息"
            allow-clear
          />
        </a-form-model-item>
      </a-form-model>
    </a-modal>

    <a-drawer
      title="修改分组信息"
      :width="550"
      :visible="editVisible"
      :body-style="{ paddingBottom: '80px' }"
      @close="onClose"
    >
      <a-form-model ref="editForm" :model="editForm" :rules="editRules">
        <a-form-model-item label="分配用户" prop="users">
          <a-select
            placeholder="请选择分配用户"
            v-model="editForm.users"
            mode="multiple"
            style="width: 100%"
            allow-clear
          >
            <a-select-option
              v-for="(d, index) in userData"
              :key="index"
              :value="d.userName + '_' + d.userId"
              >{{ d.userName }}</a-select-option
            >
          </a-select>
        </a-form-model-item>

        <a-form-model-item label="设备" prop="devId">
          <a-select
            placeholder="请选择设备分组"
            v-model="editForm.devId"
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
        <a-form-model-item label="分组名" prop="groupName">
          <a-input
            v-model="editForm.groupName"
            placeholder="请输入分组名"
            allow-clear
          />
        </a-form-model-item>

        <a-form-model-item label="备注">
          <a-input
            v-model="editForm.remark"
            placeholder="请输入备注信息"
            allow-clear
          />
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
        <a-button type="primary" :loading="editLoading" @click="SubmitEditForm">
          Submit
        </a-button>
      </div>
    </a-drawer>
  </a-card>
</template>

<script>
import {
  updateDeviceGroup,
  getDeviceGroup,
  addGroup,
  deviceList,
  listUser,
} from "@/api/module/DeviceGroup";
// import CreateForm from './modules/CreateForm'
import { tableMixin } from "@/store/table-mixin";
import { mapGetters } from "vuex";

export default {
  name: "Facility",
  components: {
    // CreateForm
  },
  mixins: [tableMixin],
  data() {
    return {
      editVisible: false,
      deviceData: [],
      userData: [],
      editForm: {
        groupName: "",
        devId: [],
        remark: "",
        users: [],
      },
      editLoading: false,
      editRules: {
        groupName: [
          { required: true, message: "请输入分组名", trigger: "blur" },
        ],
        devId: [{ required: true, message: "请选择设备", trigger: "change" }],
        users: [
          { required: true, message: "请选择分配用户", trigger: "change" },
        ],
      },
      editId: "",
      groupVisableShow: false,
      submitLoading: false,
      groupOption: [],
      form: {
        groupName: "",
        devId: [],
        remark: "",
        users: [],
      },
      rules: {
        groupName: [
          { required: true, message: "请输入分组名", trigger: "blur" },
        ],
        devId: [{ required: true, message: "请选择设备", trigger: "change" }],
        users: [
          { required: true, message: "请选择分配用户", trigger: "change" },
        ],
      },
      list: [],
      selectedRowKeys: [],
      selectedRows: [],
      multiple: true,
      ids: [],
      loading: false,
      total: 0,
      // 查询参数
      queryParam: {
        pageNum: 1,
        pageSize: 100,
      },
      columns: [
        {
          title: "分组名",
          dataIndex: "groupName",
          ellipsis: true,
          width: 200,
          align: "center",
        },
        {
          title: "备注",
          dataIndex: "remark",
          width: 150,
          ellipsis: true,
          align: "center",
        },
        {
          title: "设备数量",
          dataIndex: "deviceCount",
          ellipsis: true,
          width: 110,
          align: "center",
        },
        {
          title: "创建时间",
          dataIndex: "createTime",
          width: 200,

          ellipsis: true,
          align: "center",
        },
        {
          title: "分配用户",
          dataIndex: "users",
          scopedSlots: { customRender: "users" },
          align: "left",
        },
        {
          title: "设备ID",
          dataIndex: "devId",
          scopedSlots: { customRender: "devId" },
          align: "left",
        },
        // {
        //   title: '状态',
        //   dataIndex: 'status',
        //   scopedSlots: { customRender: 'status' },
        //   ellipsis: true,
        //   align: 'center'
        // },
        {
          title: "操作",
          dataIndex: "operation",
          width: "18%",
          scopedSlots: { customRender: "operation" },
          align: "center",
        },
      ],
    };
  },
  filters: {},
  created() {
    this.getList();
  },

  methods: {
    /** 查询搜索日志列表 */
    getList() {
      this.loading = true;

      deviceList({
        pageNum: 1,
        pageSize: 9999,
      }).then((response) => {
        this.deviceData = response.rows;
      });
      listUser({
        pageNum: 1,
        pageSize: 9999,
      }).then((response) => {
        this.userData = response.rows;
      });

      getDeviceGroup().then((response) => {
        this.loading = false;
        this.list = response.data;
      });
    },

    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParam.pageNum = 1;
      this.getList();
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
        pageSize: 10,
      };
      this.handleQuery();
    },

    changeGroup() {
      this.groupVisableShow = true;
    },
    onShowSizeChange(current, pageSize) {
      this.queryParam.pageSize = pageSize;
      this.getList();
    },
    changeSize(current, pageSize) {
      this.queryParam.pageNum = current;
      this.queryParam.pageSize = pageSize;
      this.getList();
    },
    onSelectChange(selectedRowKeys, selectedRows) {
      this.selectedRowKeys = selectedRowKeys;
      this.selectedRows = selectedRows;
      this.ids = this.selectedRows.map((item) => item.id);
      this.multiple = !selectedRowKeys.length;
    },

    submitForm() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          let data = {
            remark: this.form.remark,
            groupName: this.form.groupName,
            devId: this.form.devId.join(","),
            users: this.form.users.join(","),
          };
          addGroup(data)
            .then((response) => {
              this.form.remark = undefined;
              this.form.groupName = undefined;
              this.$message.success("添加完成", 3);
              this.getList();
              this.groupVisableShow = false;
            })
            .finally(() => {
              this.groupVisableShow = false;
            });
        } else {
          return false;
        }
      });
    },
    cancel() {
      this.groupVisableShow = false;
      this.form.remark = undefined;
      this.form.groupName = undefined;
      this.form.devId = [];
      this.form.users = [];
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      var that = this;
      const ids = row.id || this.ids;
      this.$confirm({
        title: "确认删除所选中数据?",
        content: "当前选中编号为" + ids + "的数据",
        onOk() {
          return delInquireall(ids).then(() => {
            that.onSelectChange([], []);
            that.getList();
            that.$message.success("删除成功", 3);
          });
        },
        onCancel() {},
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      var that = this;
      this.$confirm({
        title: "是否确认导出?",
        content: "此操作将导出当前条件下所有数据而非选中数据",
        onOk() {
          that.download(
            "module/inquireall/export",
            {
              ...that.queryParam,
            },
            `inquireall_${new Date().getTime()}.xlsx`
          );
        },
        onCancel() {},
      });
    },

    handleUpdate(record) {
      this.editId = record.id;
      this.editVisible = true;
      this.editForm.groupName = record.groupName;
      this.editForm.remark = record.remark ? record.remark : undefined;
      this.editForm.devId = record.devId ? record.devId.split(",") : [];
      this.editForm.users = record.users ? record.users.split(",") : [];
    },

    onClose() {
      this.editVisible = false;
      this.editId = null;
      this.editForm = {
        deviceName: null,
        groupIds: undefined,
      };
    },

    SubmitEditForm() {
      this.$refs.editForm.validate((valid) => {
        if (valid) {
          this.editLoading = true;
          let data = {
            id: this.editId,
            groupName: this.editForm.groupName,
            remark: this.editForm.remark,
            devId: this.editForm.devId.join(","),
            users: this.editForm.users.join(","),
          };
          updateDeviceGroup(data)
            .then((response) => {
              this.$message.success("已修改完成", 3);
              this.getList();
              this.editVisible = false;
            })
            .finally(() => {
              this.editLoading = false;
            });
        } else {
          return false;
        }
      });
    },
  },
};
</script>
