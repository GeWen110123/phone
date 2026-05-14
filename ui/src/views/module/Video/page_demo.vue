<template>
  <a-card :bordered="false">
    <div class="table-operations">
      <a-button type="primary" @click="$refs.CreateForm.handleAdd()">
        <a-icon type="plus" />新增任务
      </a-button>

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

      <span slot="operation" slot-scope="text, record">
        <a @click="$refs.PlayVideo.handleUpdate(record)">
          <a-icon type="eye" />查看视频
        </a>
        <a-divider type="vertical" />
        <a @click="$refs.PlayImg.handleUpdate(record)">
          <a-icon type="eye" />查看图片
        </a>
        <a-divider type="vertical" />
        <a @click="$refs.Comment.showChatModal(record.content)">
          <a-icon type="eye" />查看评论
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
    <play-video ref="PlayVideo" />
    <play-img ref="PlayImg" />
    <create-form :deviceData="deviceData" ref="CreateForm" />
    <Comment ref="Comment" />
  </a-card>
</template>

<script>
import { videoList, deviceList } from "@/api/module/Video";
import PlayVideo from "./modules/PlayVideo";
import PlayImg from "./modules/PlayImg";
import Comment from "./modules/Comment";

import CreateForm from "./modules/CreateForm";
import { tableMixin } from "@/store/table-mixin";
import { mapGetters } from "vuex";

export default {
  name: "DetailPage",
  components: {
    PlayVideo,
    PlayImg,
    CreateForm,
    Comment,
  },
  mixins: [tableMixin],
  data() {
    return {
      deviceData: [],

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
        pageSize: 10,
      },
      columns: [
        {
          title: "视频ID",
          dataIndex: "videoIndex",
          ellipsis: true,
          width: 110,
          align: "center",
        },
        {
          title: "设备ID",
          dataIndex: "devId",
          ellipsis: true,
          width: 220,
          align: "center",
        },
        {
          title: "抖音ID",
          dataIndex: "douyinId",
          ellipsis: true,
          width: 300,
          align: "center",
        },
    
        {
          title: "json",
          dataIndex: "jsonString",
          align: "left",
        },
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
        const [deviceRes, taskRes] = await Promise.all([
          deviceList({ pageNum: 1, pageSize: 9999 }),
          videoList(this.queryParam),
        ]);
        Object.assign(this, {
          deviceData: deviceRes.rows,
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
