package com.my.githubapp.parsing

import android.content.Context
import com.my.githubapp.model.Github
import org.json.JSONObject
import org.json.JSONTokener
import java.io.IOException

class ParsingJson {

    fun getJsonFromAssets(context: Context, fileName: String): String? {
        val jsonString: String
        try {
            jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }

    fun parsing(json: String): ArrayList<Github> {
        val listGithub = ArrayList<Github>()
        val jsonObject = JSONTokener(json).nextValue() as JSONObject
        val jsonArray = jsonObject.getJSONArray("users")
        for (i in 0 until jsonArray.length()) {
            val username = jsonArray.getJSONObject(i).getString("username")
            val name = jsonArray.getJSONObject(i).getString("name")
            val avatar = jsonArray.getJSONObject(i).getString("avatar")
            val company = jsonArray.getJSONObject(i).getString("company")
            val location = jsonArray.getJSONObject(i).getString("location")
            val repository = jsonArray.getJSONObject(i).getInt("repository")
            val follower = jsonArray.getJSONObject(i).getInt("follower")
            val following = jsonArray.getJSONObject(i).getInt("following")
            val github = Github(username, name, avatar, company, location, repository, follower, following)
            listGithub.add(github)
        }
        return listGithub
    }
}