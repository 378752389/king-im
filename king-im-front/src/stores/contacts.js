import {defineStore} from "pinia";
import {ref, computed} from "vue";
import {addContactAPI, getContactListAPI} from "@/http/social.js";

export const useContactsStore = defineStore('contacts', () => {
    const contactList = ref([])
    // 联系人页面中选中的联系人
    const selectedContact = ref()


    const contactListGetter = computed(() => {
        return contactList.value;
    })

    const selectedContactGetter = computed(() => {
        return selectedContact.value;
    })

    const getContact = (id) => {
        return contactList.value.find(x => x.peerId === id)
    }

    const setSelectedContact = (contact) => {
        selectedContact.value = contact
    }

    const addContact = async (contactId) => {
        await addContactAPI({
            friendId: contactId
        })
        await loadContactList()
    }

    const loadContactList = async () => {
        contactList.value = await getContactListAPI()
    }

    const isFriend = (id) => {
        return contactList.value.findIndex(contact => contact.peerId === id) > -1
    }

    const mockInit = () => {
        contactList.value = [{
            id: 100,
            userId: 1,
            peerId: 100,
            peerNickname: 'Amy',
            peerAvatar: 'https://picsum.photos/512/512?id=1',
            peerMarkName: '刘备',
            province: '广东',
            city: '深圳',
            gender: 2,
            sign: 'to be number one',
        }, {
            city: "北京",
            id: 3,
            peerAvatar: "https://picsum.photos/512/512?id=4",
            peerId: 4,
            peerMarkName: "鸡翅",
            peerNickname: "棘刺",
            province: "北京",
            userId: 1,
            gender: 1,
            sign: '',

        }]
    }

    return {contactListGetter, selectedContactGetter, getContact, addContact, setSelectedContact, loadContactList, isFriend}
})