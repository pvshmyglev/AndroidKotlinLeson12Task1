package ru.netology.nmedia

import androidx.lifecycle.MutableLiveData

class PostRepositorySQLiteImpl(private val dao: PostDao) : PostRepository {


    override val data : MutableLiveData<List<Post>> = MutableLiveData(dao.getAll())

    override fun likeById(id: Int) {

        data.value = data.value?.map {post ->
            if (post.id != id) {

                post

            } else {

                dao.save( post.copy(likeByMe = !post.likeByMe, countLikes = post.countLikes + if (post.likeByMe) -1 else 1) )

            }
        }

    }

    override fun shareById(id: Int) {

        data.value = data.value?.map {post ->
            if (post.id != id) {

                post

            } else {

                dao.save( post.copy(countShare = post.countShare + 1) )

            }
        }

    }

    override fun removeById(id: Int) {

        dao.removeById(id)

        data.value = data.value?.filter { it.id != id }

    }

    override fun saveNewPost(post: Post) {

        val listOfNewPost: List<Post> = listOf(

            dao.save(
                post.copy(
                id = 0,
                author = "Me",
                publishedDate = "Now",
                likeByMe = false
            ))
        )

        if (data.value == null) data.value = listOfNewPost else data.value?.let {
            data.value = listOfNewPost + data.value!!
        }

    }

    override fun editPost(post: Post) {

        data.value = data.value?.map {oldPost ->
            if (oldPost.id != post.id) {

                oldPost

            } else {

                dao.save(post)

            }
        }

    }

}