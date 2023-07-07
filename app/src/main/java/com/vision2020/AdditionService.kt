/*
package com.vision2020

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

class AdditionService : Service() {

    override fun onBind(intent: Intent): IBinder {

        return object : IAdditionService.Stub() {
            */
/**
             * Implementation of the add() method
             *//*

            @Throws(RemoteException::class)
            fun add(value1: Int, value2: Int): Int {
                Log.d(
                    "Service", String.format(
                        "AdditionService.add(%d, %d)", value1,
                        value2
                    )
                )
                System.exit(-1) // KILL THE PROCESS BEFORE IT CAN RESPOND
                return value1 + value2
            }
        }
    }
    interface IAdditionService {
        // You can pass values in, out, or inout.
        // Primitive datatypes (such as int, boolean, etc.) can only be passed in.
        int add(in int value1, in int value2);
    }
}

*/
