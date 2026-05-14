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
      <a-button type="primary" @click="$refs.CreateTask.handleAdd()">
        <a-icon type="plus" />新增任务
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

      <span slot="groups" slot-scope="text, record">
        <a-tag
          color="blue"
          v-for="(d, index) in record.groups ? record.groups.split(',') : []"
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
        <a @click="$refs.CreateTask.handleUpdate(record)">
          <a-icon type="edit" />修改任务
        </a>
        <a-divider type="vertical" />
        <a @click="$refs.RunTask.handleUpdate(record, deviceData)">
          <a-icon type="edit" />执行任务
        </a>
        <!-- <a-divider type="vertical" v-hasPermi="['module:inquireall:remove']" />
        <a @click="handleDelete(record)" v-hasPermi="['module:inquireall:remove']">
          <a-icon type="delete" />删除
        </a>  -->
      </span>
    </a-table>
    <!-- 分页 -->
    <a-pagination
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

    <!-- /新增/修改任务 -->
    <create-Task ref="CreateTask" @ok="getList" />
    <!-- //执行任务 -->
    <run-Task ref="RunTask" @ok="getList" />

  </a-card>
</template>

<script>
import {
  task,
  deviceList,
  groupAll,
} from "@/api/module/Strategy";
import CreateForm from "./modules/CreateForm";
import CreateTask from "./modules/AddTask";
import RunTask from "./modules/runTask.vue";
import { tableMixin } from "@/store/table-mixin";
import { mapGetters } from "vuex";

export default {
  name: "Task",
  components: {
    CreateForm,
    CreateTask,
    RunTask
  },
  mixins: [tableMixin],
  data() {
    return {
      deviceData: [],
      userData: [],

      groupOption: [],

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
          title: "ID",
          dataIndex: "id",
          ellipsis: true,
          width: 110,
          align: "center",
        },
        {
          title: "策略名称",
          dataIndex: "strategyName",
          width: 150,
          ellipsis: true,
          align: "center",
        },
        {
          title: "策略方法",
          dataIndex: "scriptPath",
          align: "left",
        },
        {
          title: "策略描述",
          dataIndex: "description",
          align: "left",
        },
        // {
        //   title: '设备ID',
        //   dataIndex: 'devId',
        //   scopedSlots: { customRender: 'devId' },
        //   align: 'left'
        // },

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
    // getList() {
    //   this.loading = true;
    //   deviceList({
    //     pageNum: 1,
    //     pageSize: 9999,
    //   }).then((response) => {
    //     this.deviceData = response.rows;
    //   });
    //   groupAll().then((response) => {
    //     this.groupOption = response.data;
    //   });
    //   task().then((response) => {
    //     this.loading = false;
    //     this.list = response.rows;
    //     this.total = response.total;
    //   });
    // },
    async getList() {
      this.loading = true;
      try {
        // 并行请求，解构结果
        const [deviceRes, groupRes, taskRes] = await Promise.all([
          deviceList({ pageNum: 1, pageSize: 9999 }),
          groupAll(),
          task(),
        ]);
        Object.assign(this, {
          deviceData: deviceRes.rows,
          groupOption: groupRes.data,
          list: taskRes.rows,
          total: taskRes.total,
        });
      } catch (err) {
        console.error("数据加载失败:", err);
      } finally {
        this.loading = false;
      }
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

    cancel() {
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
  },
};
</script>
