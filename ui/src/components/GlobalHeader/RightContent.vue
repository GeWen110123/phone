<template>
  <div :class="wrpCls">
    <a-space size="middle">
      <!-- <Searchtitle /> -->
      <!-- <screenfull /> -->
      <!-- <notice-icon /> -->
      <avatar-dropdown :menu="showMenu" :current-user="currentUser" :class="prefixCls" />
      <!-- 暂只支持中文，国际化可自行扩展 -->
    </a-space>
  </div>
</template>

<script>
import AvatarDropdown from './AvatarDropdown'
import NoticeIcon from '@/components/NoticeIcon'



export default {
  name: 'RightContent',
  components: {
    AvatarDropdown,
    NoticeIcon

  },
  props: {
    prefixCls: {
      type: String,
      default: 'ant-pro-global-header-index-action',
    },
    isMobile: {
      type: Boolean,
      default: () => false,
    },
    topMenu: {
      type: Boolean,
      required: true,
    },
    theme: {
      type: String,
      required: true,
    },
  },
  data() {
    return {
      showMenu: true,
      currentUser: {},
      docUrl: '',
      githubUrl: '',
    }
  },
  computed: {
    wrpCls() {
      return {
        'ant-pro-global-header-index-right': true,
        [`ant-pro-global-header-index-${this.isMobile || !this.topMenu ? 'light' : this.theme}`]: true,
      }
    },
  },
  mounted() {
    setTimeout(() => {
      this.currentUser = {
        name: '',
      }
    }, 1500)
  },
  methods: {
    toDoc() {
      window.open(this.docUrl)
    },
    toGithub() {
      window.open(this.githubUrl)
    },
  },
}
</script>
