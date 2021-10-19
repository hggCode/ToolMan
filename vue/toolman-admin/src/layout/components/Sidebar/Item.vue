<script>
// import { h } from "vue";
// import { toRefs } from 'vue';
export default {
  name: 'MenuItem',
  functional: true,
  props: {
    icon: {
      type: String,
      default: ''
    },
    title: {
      type: String,
      default: ''
    }
  },
   render() {
  // const { icon, title } = toRefs(props)
    const vnodes = []
    if (this.icon) {
        vnodes.push(<i class={[this.icon, 'sub-el-icon']} />)
    }

    if (this.title) {
       vnodes.push(<span slot='title'>{(this.title)}</span>)
    }
    return vnodes
    
  }
}
</script>

<style scoped>
.sub-el-icon {
  color: currentColor;
  width: 1em;
  height: 1em;
}
</style>
