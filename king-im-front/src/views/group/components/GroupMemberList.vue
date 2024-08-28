<script setup>
import {ref} from "vue";
import {useContactsStore} from "@/stores/contacts.js";
import {useUserStore} from "@/stores/user.js";
import KingDialog from "@/components/common/KingDialog.vue";
import InviteMember from "@/views/group/components/InviteMember.vue";
import {inviteFriendJoinGroupAPI} from "@/http/social.js";
import {useGroupsStore} from "@/stores/groups.js";
import {ShowMessageBox} from "@/components/common/func/messageBox.js";

const props = defineProps({
  roomId: {
    type: Number
  },
  groupMemberList: {
    type: Array,
    default: []
  }
})

const contactInfo = ref({})
const onGroupMemberClick = (e, groupMember) => {
  const contactStore = useContactsStore()
  const userStore = useUserStore()
  if (userStore.info?.id === groupMember.id) {
    console.log(groupMember)
    return
  }
  let contact = contactStore.getContact(groupMember.id)
  if (contact == null) {
    contact = {
      id: groupMember.id,
      peerNickname: groupMember.name,
      peerAvatar: groupMember.avatar,
    }
  }
  contactInfo.value = contact
  console.log(contactInfo.value)
}


const addMemberDialogRef = ref()
const onAddMemberClick = () => {
  addMemberDialogRef.value.open()
}

const inviteMemberRef = ref()
const onConfirmInviteMembers = async () => {
  const data = inviteMemberRef.value.selectedList
  ShowMessageBox({
    message: `请确认是否邀请 <span style='color: red;'>${data.map(friend => friend.peerNickname).join(', ')}</span> 进群?`,
    confirm: async () => {
      await inviteFriendJoinGroupAPI({
        roomId: props.roomId,
        friendIds: data.map(item => item.peerId).join(',')
      })
      await useGroupsStore().loadGroupList()
      addMemberDialogRef.value.close()
    }
  })

}

</script>

<template>
  <div ref="groupMemberList" class="group-member-list">
    <div v-for="groupMember in groupMemberList" :key="groupMember.id" class="group-member pointer-select">
      <img @click="onGroupMemberClick($event, groupMember)" :src="groupMember.avatar" alt=""/>
      <div class="group-member-name">
        <div>{{ groupMember.name }}</div>
      </div>
    </div>

    <div @click="onAddMemberClick" class="add-member">
      <i class="iconfont icon-add"></i>
    </div>

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