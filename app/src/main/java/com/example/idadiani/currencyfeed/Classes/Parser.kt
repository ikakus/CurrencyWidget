package com.example.idadiani.currencyfeed.Classes

/**
 * Created by i.dadiani on 2/23/2016.
 */

import android.os.AsyncTask
import android.util.Log
import com.example.idadiani.currencyfeed.Interfaces.LoadingDoneListener
import com.google.gson.annotations.SerializedName
import com.stanfy.gsonxml.GsonXmlBuilder
import com.stanfy.gsonxml.XmlParserCreator
import org.w3c.dom.Element

import org.xml.sax.InputSource
import org.xmlpull.v1.XmlPullParserFactory
import java.io.StringReader
import java.util.*
import javax.xml.parsers.DocumentBuilderFactory


class Parser {

    var loadingListener: LoadingDoneListener? = null

    fun parse(loadingDone : LoadingDoneListener?) {
        GetXml().execute();
        loadingListener = loadingDone
    }

    inner class GetXml : AsyncTask<Void, Void, MutableList<Record>>() {
        override fun doInBackground(vararg p0: Void?): MutableList<Record>? {
            var networkClient = NetworkClient()
            var parserCreator = XmlParserCreator { XmlPullParserFactory.newInstance().newPullParser() }
            var gsonXml = GsonXmlBuilder()
                    .setXmlParserCreator(parserCreator)
                    .create()

            val xml = networkClient.get(Constants.NBG_URL)

            var response = gsonXml.fromXml(xml, Response::class.java)
            var description = removeImageTags(response.channel?.item?.Description)

            var dbf = DocumentBuilderFactory.newInstance()
            var db = dbf.newDocumentBuilder()
            var inputSource = InputSource(StringReader(description))
            var doc = db.parse(inputSource)
            doc.documentElement.normalize()

            var nodeList = doc.getElementsByTagName("table")
            var rows = nodeList.item(0).childNodes;

            var records :MutableList<Record> = mutableListOf()

            for (i in 0..rows.length-1) {

                var node  = rows.item(i) as Element
                var columns = node.getElementsByTagName("td")
                var record = Record()

                for(d in 0..columns.length-1){
                    var c = columns.item(d).childNodes.item(0)
                    var value = c.nodeValue
                    record.data?.add(value)
                }
                records.add(record)
                Log.i("Data ", record.data.toString())

            }

            Log.i("Parsed ", Date().toString())

            return records;
        }

        override fun onPostExecute(result: MutableList<Record>?) {
            super.onPostExecute(result)
            loadingListener?.loaded(result as MutableList<Record>)

        }
    }

    private fun removeImageTags(description: String?): String? {
        return description?.replace("<td><img  src=\"https://www.nbg.gov.ge/images/green.gif\"></td>", "")?.
                replace("<td><img  src=\"https://www.nbg.gov.ge/images/red.gif\"></td>", "")
    }

    private fun closeImageTags(description: String?): String? {
        return description?.replace("<td><img  src=\"https://www.nbg.gov.ge/images/green.gif\"></td>", "<td><img  src=\"https://www.nbg.gov.ge/images/green.gif\"></img></td>")?.
                replace("<td><img  src=\"https://www.nbg.gov.ge/images/red.gif\"></td>", "<td><img  src=\"https://www.nbg.gov.ge/images/red.gif\"></img></td>")
    }

    class Record{
        val data: MutableList<String>? = mutableListOf()
    }

    class Response {

        @SerializedName("channel")
        var channel: Channel? = null

    }

    class Item {

        @SerializedName("title")
        val Title: String = ""
        @SerializedName("link")
        val Link: String = ""
        @SerializedName("description")
        val Description: String = ""
        @SerializedName("pubDate")
        val PubDate: String = ""
        @SerializedName("channel")
        val Guid: String = ""

    }

    class Channel {

        @SerializedName("item")
        var item: Item? = null
        @SerializedName("description")
        var description: String = ""
        @SerializedName("title")
        var title: String = ""

    }
}

