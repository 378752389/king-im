import {defineStore} from "pinia";
import {computed, ref} from "vue";
import {
    addGroupAPI,
    deleteGroupAPI,
    getGroupListAPI,
    inviteFriendJoinGroupAPI,
    modifyGroupAPI,
    quitGroupAPI
} from "@/http/social.js";
import {genPicture} from "@/utils/picUtils.js";
import {useChatsStore} from "@/stores/chats.js";
import {buildNoticeMessage} from "@/utils/msgUtils.js";
import {useUserStore} from "@/stores/user.js";

export const useGroupsStore = defineStore('groups', () => {
    const groupList = ref([])
    // 群列表页面中选中的群
    const selectedGroup = ref()

    const groupListGetter = computed(() => {
        return groupList.value;
    })

    const selectedGroupGetter = computed(() => {
        return selectedGroup.value;
    })
    const getGroup = (id) => {
        return groupList.value.find(x => x.roomId === id)
    }

    const setSelectedGroup = (group) => {
        selectedGroup.value = group
    }

    const createGroup = async (members) => {
        const roomMemberList = members.map(member => {
            return {
                id: member.peerId
            }
        })
        let groupLeaderName = useUserStore().info?.nickName
        let memberNames = members.map(member => member.peerNickname).join(", ")

        let name = "";
        if (memberNames.length > 16) {
            name = memberNames.substring(16) + "..." + "的群聊"
        } else {
            if (memberNames.length > 0) {
                name = groupLeaderName + ", " + memberNames + "的群聊"
            } else {
                name = groupLeaderName + "的群聊"
            }
        }

        const groupId = await addGroupAPI({
            name,
            roomMemberList,
        })

        let msg = buildNoticeMessage({
            type: 2,
            roomId: groupId,
            fromUid: useUserStore().info?.id,
            content: `${groupLeaderName} 创建了群聊\n，并邀请了 ${memberNames}  加入群聊`
        })
        await loadGroupList()
        useChatsStore().insertMessage(groupId, 2, msg)
    }

    const loadGroupList = async () => {
        const groupListData = await getGroupListAPI()
        if (groupListData != null) {
            groupListData.map(group => {
                if (group.avatar == null) {
                    group.avatar = genPicture(group.name)
                }
                return group;
            })
        }
        groupList.value = groupListData
    }

    const deleteGroup = async (groupId) => {
        await deleteGroupAPI({
            roomId: groupId
        })
        useChatsStore().removeChat(groupId, 2)
        await loadGroupList()
    }

    const inviteFriendJoinGroup = async (roomId, members) => {
        await inviteFriendJoinGroupAPI({
            roomId: roomId,
            friendIds: members.map(item => item.peerId).join(',')
        })
        await useGroupsStore().loadGroupList()
    }

    const quitGroup = async (groupId) => {
        await quitGroupAPI({
            roomId: groupId
        })
        useChatsStore().removeChat(groupId, 2)
        await loadGroupList()
    }

    const modifyGroup = async ({roomId, notice, name, avatar, myName, markName}) => {
        await modifyGroupAPI({
            roomId, notice, name, avatar, myName, markName
        })
        // 修改会话页信息
        const chatStore = useChatsStore()
        const chat = chatStore.getChat(roomId, 2)
        if (chat != null) {
            chat.chatAvatar = avatar
            chat.chatName = markName
        }
    }
    const mockInit = () => {
        groupList.value = [
            {
                id: 1,
                userId: 1,
                roomId: 1,
                name: "chat群",
                limit: 200,
                myName: null,
                notice: null,
                markName: "chat群",
                createBy: "king",
                leaderId: 1,
                createTime: null,
                avatar: null,
                roomMemberList: [
                    {
                        id: 1,
                        name: "king",
                        avatar: "https://picsum.photos/512/512?id=1"
                    }
                ]
            },
        ]
    }

    return {
        // groupList,
        // selectedGroup,
        groupListGetter, selectedGroupGetter, createGroup, inviteFriendJoinGroup, deleteGroup, quitGroup, modifyGroup, setSelectedGroup, getGroup, loadGroupList
    }
}, {
    persist: true
})