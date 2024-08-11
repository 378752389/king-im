<script setup>

</script>

<template>
  <div class="wrapper">

    <div class="post-header">
      <div class="left">
        <img class="avatar" src="https://picsum.photos/512/512?id=217" alt="avatar">
        <div class="info">
          <div class="nickname">爱摸狗头<span class="level">Lv.12</span><span style="color: dodgerblue">铁杆吧友</span>
          </div>
          <div><span>2023-04-01</span> <span>IP属地：广东</span></div>
        </div>
      </div>
      <div class="right">关注</div>
    </div>
    <div class="post">
      <div class="post-body">
        <h1 class="post-title">开贴记录一下，被狗通外包释放的日子</h1>
        <div class="post-content">Lorem ipsum dolor sit amet, consectetur adipisicing elit. Aspernatur deserunt dolorem
          eligendi error, eum excepturi laudantium nam natus nesciunt placeat repellat tempora ut voluptas. At aut
          consequuntur dicta, earum eligendi explicabo illo ipsa iure maiores maxime minus nobis nulla provident, quas,
          sapiente sint tempora veritatis. Accusantium consectetur ea expedita magni minus nobis odio quae repellendus
          voluptas. Aliquam amet asperiores debitis est explicabo, ipsa laudantium magnam minus mollitia nemo nisi
          reprehenderit sequi sunt ut vero vitae voluptatum! Aliquam asperiores autem beatae blanditiis consectetur cum
          cumque distinctio dolorum ea earum eos ex expedita facere facilis hic id impedit labore minus nisi nobis
          officiis placeat praesentium provident quia quis quos ratione reiciendis saepe sint sit tempora voluptas,
          voluptate voluptates. Aliquid consectetur cumque deleniti ex inventore maxime nisi rem reprehenderit sequi
          velit. Ad, aut consequuntur corporis cum, deleniti dolorem dolorum ducimus eos, eum iure iusto maxime minima
          modi mollitia numquam optio placeat quae quaerat quam qui repellendus sed totam unde ut voluptatem. Adipisci
          deserunt dicta ducimus eveniet facere, fuga ipsum neque non nostrum odio quasi quis ratione reiciendis sequi
          tempore temporibus veritatis, vitae voluptatem. Deserunt esse exercitationem expedita minima quae quisquam
          ratione? Dolore id iure iusto libero modi officiis perferendis repellat soluta veniam vero.
        </div>
      </div>
    </div>

    <hr/>

    <div class="comment" v-for="id in 4">
      <img class="avatar" :src="`https://picsum.photos/512/512?id=${id}`" alt=""/>

      <div class="info">
        <div class="info-text">
          <div class="row">
            <div class="left">
              <span class="nickname">能吃能跑</span>
              <span class="level">Lv.8</span>
              <span class="tag"></span>
            </div>
            <div class="right">
              <span class="prise">点赞</span>
              <span class="more">更多</span>
            </div>
          </div>
          <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed euismod, nunc id ultricies lacinia, nisl nunc
            tincidunt nunc, id lacinia nunc nunc id nunc. Sed euismod, nunc id ultricies lacinia, nisl nunc tincidunt
            nunc, id lacinia nunc nunc id nunc.</p>

          <div class="location">
            <span> 05-21</span>
            <span> 深圳</span>
          </div>

          <div class="reply">

            <div class="reply-item"><span class="reply-nickname">用户0</span>Lorem ipsum dolor sit amet, consectetur
              adipisicing elit. A asperiores aut culpa dolore enim fugit molestias omnis porro quaerat temporibus, ullam
              voluptatum? Aliquam architecto, cum, deleniti dolorem fugit iure laudantium omnis perferendis placeat quae
              quaerat ratione tenetur velit! Deleniti dolore harum suscipit! Cumque ex harum id in inventore nulla,
              perferendis quibusdam quod sit. Consequatur culpa doloribus eaque enim ipsum iusto nostrum, nulla omnis
              pariatur praesentium quasi quo, reprehenderit! Consectetur, modi.
            </div>

            <div class="reply-item" v-for="count in 3"><span class="reply-nickname">用户{{ count }}</span>Lorem ipsum
              dolor sit amet, consectetur adipisicing elit. Aut consequatur consequuntur cumque debitis ducimus eaque
              enim facilis ipsam itaque libero maxime, mollitia natus omnis placeat quia quos tempore voluptate
              voluptatum.
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="fixed">

    </div>
  </div>
</template>

<style scoped lang="stylus">
.wrapper
  width 800px
  margin auto

  .post-header
    position sticky
    top 0
    background-color white
    border-bottom 2px solid black
    display flex
    justify-content space-between
    padding 10px 0

    .left
      display flex

      .avatar
        background-color dodgerblue
        margin-right 20px
        width 60px
        height 60px
        border-radius 50%

      .info
        display flex
        flex-direction column
        justify-content space-around

        span
          margin 5px

          &.level
            color grey
            font-size 0.9rem

    .right
      margin-top 5px

  .post
    .post-body
      margin-top 20px

      .post-title
        margin 20px 10px

      .post-content
        padding 0 10px
        margin-bottom 50px

  .comment
    display flex
    justify-content space-between
    padding 10px
    margin 10px
    border 1px solid black
    border-radius 10px

    .avatar
      background-color dodgerblue
      width: 50px
      height: 50px
      border-radius 50%
      flex-shrink 0

    .info
      display flex

      .info-text
        margin-left 20px

        .row
          display flex
          justify-content space-between

          .left span
            margin-right 10px

          .right span
            margin-left 10px

        p
          margin 10px 0

        .location
          margin 10px 0

        .reply
          border 1px solid black
          border-radius 1rem
          background-color rgba(0, 0, 0, 0.1)
          padding 10px


          .reply-item
            margin 5px 0
            overflow hidden
            text-overflow ellipsis
            display -webkit-box
            -webkit-line-clamp 3
            -webkit-box-orient vertical

            &:hover
              cursor pointer

            .reply-nickname
              &::after
                content ":"
                margin 0 5px


  .fixed
    width: 100px
    height 100px
    position fixed
    top 200px
    right 50px
    background-color red
</style>