package ru.netology.nmedia

interface PostDao {

    fun getAll(): List<Post>
    fun removeById(id: Int)
    fun save(post: Post) : Post

}