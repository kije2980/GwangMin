// IScoketService.aidl
package com.example.smarthotelservice;

// Declare any non-default types here with import statements

interface IScoketService {
    void Connect();
    void disConnect();
    void Send(String msg);
    boolean isConnect();
    void Recv();
}
