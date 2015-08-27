package com.lizhuo.kotlinlearning

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.google.gson.Gson
import com.lizhuo.kotlinlearning.Model.ConstantName
import com.lizhuo.kotlinlearning.Model.GanHuo
import com.squareup.okhttp.Callback
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.Response
import java.io.IOException

/**
 * Created by lizhuo on 15-8-26.
 */

public class GanHuoFragment(type: Int): Fragment() {

    private var ganhuo = GanHuo()
    private var type: Int = 0
    private var recy: RecyclerView ?=null
    private var handler: Handler ?=null

    init {
        this.type = type
        handler = object: Handler(){
            override fun handleMessage(msg: Message) {
                when (msg.what){
                    1 -> {
                        var adapter = GanHuoAdapter(getActivity().getApplicationContext(),ganhuo)
                        recy?.setAdapter(adapter)
                    }
                }
            }
        };
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var rootView = inflater.inflate(R.layout.fragment_ganhuo,container,false)
        recy = rootView.findViewById(R.id.ganhuo_recy) as RecyclerView
        return rootView
    }

    override fun onStart() {
        super.onStart()
        GetGanHuoByType()
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        val layoutManager = LinearLayoutManager(getActivity())
        recy?.setLayoutManager(layoutManager)
    }

    fun GetGanHuoByType() {
        var getGanHuoUrl: String = ""
        when (this.type) {
            ConstantName.ALL -> getGanHuoUrl += ConstantName.REQUEST_URL+"/all/3/1"
            ConstantName.ANDROID -> getGanHuoUrl += ConstantName.REQUEST_URL+"/Android/3/1"
            ConstantName.IOS -> getGanHuoUrl += ConstantName.REQUEST_URL+"/iOS/3/1"
            ConstantName.FRONT_END-> getGanHuoUrl += ConstantName.REQUEST_URL+"/前端/3/1"
            ConstantName.EXTANTION_RESUSE-> getGanHuoUrl += ConstantName.REQUEST_URL+"/拓展资源/3/1"
            ConstantName.REST_VIDEO-> getGanHuoUrl += ConstantName.REQUEST_URL+"/休息视频/3/1"
        }

        val gson = Gson()
        var client = OkHttpClient();
        val request = Request.Builder().url(getGanHuoUrl).build()

        client.clone().newCall(request).enqueue(object : Callback {
            override fun onResponse(response: Response) {
                var res = response.body().string()
                ganhuo = gson.fromJson(res, javaClass<GanHuo>())
                handler?.sendEmptyMessage(1)
            }
            override fun onFailure(request: Request?, e: IOException?) {
                handler?.sendEmptyMessage(2)
            }
        });
    }

}