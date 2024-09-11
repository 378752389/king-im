<script setup>
import {reactive, ref} from "vue";
import {useContactsStore} from "@/stores/contacts.js";
import {useUserStore} from "@/stores/user.js";
import KingDialog from "@/components/common/KingDialog.vue";
import InviteMember from "@/views/group/components/InviteMember.vue";
import {inviteFriendJoinGroupAPI} from "@/http/social.js";
import {useGroupsStore} from "@/stores/groups.js";
import {ShowMessageBox} from "@/components/common/func/messageBox.js";
import {ShowToast} from "@/components/common/func/toast.js";
import KingContextMenu from "@/components/common/KingContextMenu.vue";
import KingContextMenuItem from "@/components/common/KingContextMenuItem.vue";
import {useChatsStore} from "@/stores/chats.js";
import {useRouter} from "vue-router";

const props = defineProps({
  roomId: {
    type: Number
  },
  groupMemberList: {
    type: Array,
    default: []
  }
})

const chatStore = useChatsStore()
const contactStore = useContactsStore()
const router = useRouter()

const memberContextMenuData = reactive({
  contactInfo: {},
  isFriend: false,
  isSelf: false
})
const onGroupMemberClick = (e, groupMember) => {
  const contactStore = useContactsStore()
  const userStore = useUserStore()
  if (userStore.info?.id === groupMember.id) {
    // console.log(groupMember)
    memberContextMenuData.isSelf = true
  } else {
    memberContextMenuData.isSelf = false
  }
  let contact = contactStore.getContact(groupMember.id)
  if (contact == null) {
    memberContextMenuData.isFriend = false
    contact = {
      id: groupMember.id,
      peerNickname: groupMember.name,
      peerAvatar: groupMember.avatar,
    }
  } else {
    memberContextMenuData.isFriend = true
  }

  memberContextMenuData.contactInfo = contact
  console.log(memberContextMenuData.contactInfo)
  memberContextMenuRef.value.open(e, memberContextMenuData.contactInfo)
}


const addMemberDialogRef = ref()
const onAddMemberClick = () => {
  addMemberDialogRef.value.open()
}

const inviteMemberRef = ref()
const onConfirmInviteMembers = async () => {
  const roomId = props.roomId
  const data = inviteMemberRef.value.selectedList
  const memberNames = data.map(friend => friend.peerNickname).join(', ')
  ShowMessageBox({
    message: `请确认是否邀请 <span style='color: red;'>${memberNames}</span> 进群?`,
    confirm: async () => {
      await useGroupsStore().inviteFriendJoinGroup(roomId, data)
      addMemberDialogRef.value.close()
      ShowToast({
        message: `邀请好友 ${memberNames} 进群成功！`,
        type: 'success'
      })
    }
  })

}


const memberContextMenuRef = ref()
const addFriendClick = (context) =>{
  ShowMessageBox({
    message: `请确认是否添加 ${context.peerNickname} 为好友？`,
    confirm:  async () => {
      await contactStore.addContact(context.id)
      ShowToast({
        message: "添加好友成功",
        type: "success"
      })
    }
  })
  memberContextMenuRef.value.close()
}
const detailClick = (context) =>{
  const contact = contactStore.getContact(context.peerId)
  contactStore.setSelectedContact(contact)
  router.push({
    name: "contact"
  })
  memberContextMenuRef.value.close()
}
const sendMessageClick = (context) =>{
  chatStore.setChat(context.peerId, 1)
  router.push({
    name: 'chat'
  })
  memberContextMenuRef.value.close()
}

const modifyInfoClick = (context) => {
  router.push({
    name: 'profile'
  })
  memberContextMenuRef.value.close()
}
</script>

<template>
  <div ref="groupMemberList" class="group-member-list">
    <div v-for="groupMember in groupMemberList" :key="groupMember.id" class="group-member pointer-select">
      <img :src="groupMember.avatar" alt=""/>
      <div @click="onGroupMemberClick($event, groupMember)" class="group-member-name">
        <div>{{ groupMember.name }}</div>
      </div>
    </div>

    <div @click="onAddMemberClick" class="add-member">
      <i class="iconfont icon-add"></i>
    </div>

    <king-context-menu ref="memberContextMenuRef">
      <template #default="{context}">
        <king-context-menu-item v-if="!memberContextMenuData.isFriend && !memberContextMenuData.isSelf" name="加好友" @item-click="addFriendClick(context)"  />
        <king-context-menu-item v-if="memberContextMenuData.isFriend && !memberContextMenuData.isSelf" name="详情" @item-click="detailClick(context)" />
        <king-context-menu-item v-if="memberContextMenuData.isFriend && !memberContextMenuData.isSelf" name="发消息" @item-click="sendMessageClick(context)" />
        <king-context-menu-item v-if="memberContextMenuData.isSelf" name="修改信息" @item-click="modifyInfoClick(context)" />
      </template>
    </king-context-menu>

    <!--    todo 邀请组成员-->
    <king-dialog @confirm="onConfirmInviteMembers" ref="addMemberDialogRef">
      <invite-member :current-member-list="groupMemberList" ref="inviteMemberRef"/>
    </king-dialog>
  </div>
</template>

<style scoped lang="stylus">
.group-member-list
  display flex

  .group-member
    margin 5px 5px

    img
      width 80px
      height 80px

    .group-member-name
      display flex
      justify-content center

      div
        max-width 70px
        overflow hidden
        white-space nowrap
        text-overflow ellipsis

  .add-member
    width 80px
    height 80px
    margin 5px 5px
    display flex
    justify-content center
    align-items center
    background-color rgba(0, 0, 0, 0.1)

    i
      color grey
      font-size 50px

</style>