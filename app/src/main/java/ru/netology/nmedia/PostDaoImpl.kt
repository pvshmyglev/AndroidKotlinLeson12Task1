package ru.netology.nmedia

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

class PostDaoImpl(private val db: SQLiteDatabase) : PostDao {

    companion object{

        const val DDL = """
                CREATE TABLE ${PostColumns.TABLE} (
                ${PostColumns.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
                ${PostColumns.COLUMN_AUTHOR} TEXT NOT NULL,
                ${PostColumns.COLUMN_CONTENT} TEXT NOT NULL,
                ${PostColumns.COLUMN_VIDEO} TEXT NOT NULL,
                ${PostColumns.COLUMN_PUBLISHED_DATE} TEXT NOT NULL,
                ${PostColumns.COLUMN_LIKE_BY_ME} BOOLEAN NOT NULL DEFAULT false,
                ${PostColumns.COLUMN_COUNT_LIKES} INTEGER NOT NULL DEFAULT 0,
                ${PostColumns.COLUMN_COUNT_SHARE} INTEGER NOT NULL DEFAULT 0,
                ${PostColumns.COLUMN_COUNT_VISIBILITY} INTEGER NOT NULL DEFAULT 0
                );"""

    }

    object PostColumns {

        const val TABLE = "posts"
        const val COLUMN_ID = "id"
        const val COLUMN_AUTHOR = "author"
        const val COLUMN_CONTENT = "content"
        const val COLUMN_VIDEO = "video"
        const val COLUMN_PUBLISHED_DATE = "publishedDate"
        const val COLUMN_LIKE_BY_ME = "likeByMe"
        const val COLUMN_COUNT_LIKES = "countLikes"
        const val COLUMN_COUNT_SHARE = "countShare"
        const val COLUMN_COUNT_VISIBILITY = "countVisibility"

        val ALL_COLUMNS = arrayOf(
            COLUMN_ID,
            COLUMN_AUTHOR,
            COLUMN_CONTENT,
            COLUMN_VIDEO,
            COLUMN_PUBLISHED_DATE,
            COLUMN_LIKE_BY_ME,
            COLUMN_COUNT_LIKES,
            COLUMN_COUNT_SHARE,
            COLUMN_COUNT_VISIBILITY
        )

    }

    override fun getAll(): List<Post> {

        val posts = mutableListOf<Post>()

        if (tableEmpty(db, PostColumns.TABLE)) {

            save(Post(
                    0,
                    "Наименование автора для примера, немного длинее чтобы обрезать!",
                    "18–20 апреля в Москве проходила I Международная Ассамблея Российской академии образования «Ученик в современном мире: формула успеха». Миссия Ассамблеи заключалась в акцентуации новых и оптимизации имеющихся подходов к внедрению на уровнях основного общего и среднего общего образования релевантных методов обучения и воспитания, образовательных технологий, отвечающих запросам современного общества, создание эффективной среды для личностного и предпрофессионального развития обучающихся, личностного и профессионального развития педагогов. http://www.ivanovo.ac.ru/about_the_university/news/11502/",
                    "",
                    "15 мая 2022 года. 14:50:34",
                    false,
                    999,
                    2000,
                    131
                ))
            save(Post(
                    0,
                    "Наименование автора для примера, немного длинее чтобы обрезать!",
                    "Тест, второй пост",
                    "https://www.youtube.com/watch?v=JDxuBbsua_E",
                    "16 мая 2022 года. 14:50:34",
                    true,
                    121,
                    995,
                    1000000
                ))
            save(Post(
                    0,
                    "Наименование автора для примера, немного длинее чтобы обрезать!",
                    "Как писать крутые посты в VK, чтобы их сохраняли, лайкали и комментировали\n" +
                            "Вкусные тексты, которые взахлеб читают и делятся ими с друзьями - это ли не мечта каждого, кто продвигает свой аккаунт в социальных сетях?\n" +
                            "В VK есть свои нюансы, и их нужно учитывать, когда пишите посты. Людям не нужны сложные, заумные статьи. Они заходят сюда отдохнуть, расслабиться и почитать нечто легкое и увлекательное. Современный человек не может долго удерживать свое внимание, он читает бегло и быстро теряет интерес. Поделюсь своим опытом каким правилам следовал чтобы увеличить аудиторию группы магазина Фитнес Элита. Задача была не просто получить новых подписчиков, а сделать из них клиентов. Ниша спортивного питания сильно переполнена и чтобы привлечь подписчика, а впоследствии сделать из него покупателя нужно что-то уникальное.",
                    "",
                    "17 мая 2022 года. 14:50:34",
                    false,
                    999,
                    2000,
                    131
                ))
            save(Post(
                    0,
                    "Наименование автора для примера, немного длинее чтобы обрезать!",
                    "Тест, четвертый пост",
                    "",
                    "18 мая 2022 года. 14:50:34",
                    false,
                    999,
                    2000,
                    131
                ))
            save(Post(
                    0,
                    "Наименование автора для примера, немного длинее чтобы обрезать!",
                    "Тест, пятый пост",
                    "",
                    "19 мая 2022 года. 14:50:34",
                    false,
                    999,
                    2000,
                    131
                ))
        }

        db.query(
            PostColumns.TABLE,
            PostColumns.ALL_COLUMNS,
            null,
            null,
            null,
            null,
            "${PostColumns.COLUMN_ID} DESC"
        ).use {

            while (it.moveToNext()) {
                posts.add(map(it))
            }

        }

        return posts

    }



    override fun save(post: Post) : Post {

        val values = getValueOfPost(post)

        val id = db.replace(PostColumns.TABLE, null, values)

        db.query(
            PostColumns.TABLE,
            PostColumns.ALL_COLUMNS,
            "${PostColumns.COLUMN_ID} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        ).use {

            while (it.moveToNext()) {
                return  map(it)
            }

        }

        return post.copy(id = id.toInt())

    }

    private fun getValueOfPost(post: Post) : ContentValues {

        val value = ContentValues().apply {

            if (post.id != 0) {

                put(PostColumns.COLUMN_ID, post.id)

            }

            put(PostColumns.COLUMN_CONTENT, post.content)
            put(PostColumns.COLUMN_AUTHOR, post.author)
            put(PostColumns.COLUMN_COUNT_LIKES, post.countLikes)
            put(PostColumns.COLUMN_COUNT_SHARE, post.countShare)
            put(PostColumns.COLUMN_COUNT_VISIBILITY, post.countVisibility)
            put(PostColumns.COLUMN_LIKE_BY_ME, post.likeByMe)
            put(PostColumns.COLUMN_PUBLISHED_DATE, post.publishedDate)
            put(PostColumns.COLUMN_VIDEO, post.video)

        }

        return value

    }

    override fun removeById(id: Int) {
        db.delete(PostColumns.TABLE,
            "${PostColumns.COLUMN_ID} = ?",
            arrayOf(id.toString())
        )
    }

    private fun tableEmpty(db: SQLiteDatabase?, tableName: String?): Boolean {
        if (tableName == null || db == null) {
            return true
        }
        val cursorDb = db.rawQuery(
            "SELECT COUNT(*) FROM sqlite_master WHERE type = ? AND name = ?",
            arrayOf("table", tableName)
        )
        if (!cursorDb.moveToFirst()) {
            cursorDb.close()
            return true
        }

        val cursor = db.rawQuery("SELECT * FROM " + tableName + " LIMIT 1", emptyArray())

        val emp = !cursor.moveToNext()
        cursor.close()
        return emp
    }

    private fun map (cursor: Cursor): Post {
        with(cursor){
            return Post(
                getInt(getColumnIndexOrThrow(PostColumns.COLUMN_ID)),
                getString(getColumnIndexOrThrow(PostColumns.COLUMN_AUTHOR)),
                getString(getColumnIndexOrThrow(PostColumns.COLUMN_CONTENT)),
                getString(getColumnIndexOrThrow(PostColumns.COLUMN_VIDEO)),
                getString(getColumnIndexOrThrow(PostColumns.COLUMN_PUBLISHED_DATE)),
                getInt(getColumnIndexOrThrow(PostColumns.COLUMN_LIKE_BY_ME)) > 0,
                getInt(getColumnIndexOrThrow(PostColumns.COLUMN_COUNT_LIKES)),
                getInt(getColumnIndexOrThrow(PostColumns.COLUMN_COUNT_SHARE)),
                getInt(getColumnIndexOrThrow(PostColumns.COLUMN_COUNT_VISIBILITY))
            )
        }

    }


}