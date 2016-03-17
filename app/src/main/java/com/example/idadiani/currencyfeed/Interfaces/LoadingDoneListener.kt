package com.example.idadiani.currencyfeed.Interfaces

import com.example.idadiani.currencyfeed.Classes.Parser

/**
 * Created by i.dadiani on 2/26/2016.
 */

interface LoadingDoneListener {
   fun loaded(records :MutableList<Parser.Record>)
}
