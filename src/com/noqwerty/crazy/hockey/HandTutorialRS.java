/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.noqwerty.crazy.hockey;

import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;

/**
 *
 * @author Mahmoud
 */
public class HandTutorialRS {

    RecordStore recordStore;
    RecordEnumeration recordEnumeration;

    public HandTutorialRS() {
        try {
            recordStore = RecordStore.openRecordStore("crazy_hockey_handtutorial", true);
            recordEnumeration = recordStore.enumerateRecords(null, null, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void storeRecord(String currentSate) {
        byte[] store_record = currentSate.getBytes();
        try {
            recordStore.addRecord(store_record, 0, store_record.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getRecord() {
        String r = "0";
        try {
            recordEnumeration = recordStore.enumerateRecords(null, null, false);
            while (recordEnumeration.hasNextElement()) {
                byte[] record = null;
                try {
                    record = recordEnumeration.nextRecord();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                r = new String(record, 0, record.length);
                return r;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return r;
    }
}
