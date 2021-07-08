package org.eclipse.paho.mqttclient;

import java.util.Scanner;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class Client implements MqttCallback{

    public static void main(String[] args) {

        String topic        = "MQTT Examples";
        String content      = "Message from MqttPublishSample";
        int qos             = 2;
        //String broker       = "tcp://test.mosquitto.org:1883";
        String broker       = args[0];
        String clientId     = "JavaSample";
        MemoryPersistence persistence = new MemoryPersistence();

        try {
            MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setUserName(args[1]);
            connOpts.setPassword(args[2].toCharArray());
            connOpts.setKeepAliveInterval(Integer.parseInt(args[3]));;
            connOpts.setCleanSession(true);
            System.out.println("Connecting to broker: "+broker);
            sampleClient.connect(connOpts);
            System.out.println("Connected");
            Scanner oScanner = new Scanner(System.in);
            System.out.println("Enterq q to disconnect\n p to publish\n s to subscribe\n");
            String strSelection = oScanner.nextLine();
            while(oScanner.hasNextLine())
            {
              if(strSelection.equals("q"))
            	  break;
              else if(strSelection.equals("p"))
              {
            	  topic = args[4];
            	  content = args[5];
            	  qos = Integer.parseInt(args[6]);
	              System.out.println("Publishing message: "+content);
	              MqttMessage message = new MqttMessage(content.getBytes());
	              message.setQos(qos);
	              sampleClient.publish(topic, message);
	              System.out.println("Message published");
              }
              else if(strSelection.equals("s"))
              {
            	  topic = args[4];
            	  //content = args[5];
            	  qos = Integer.parseInt(args[6]);
	              System.out.println("Subscribing to topic: "+topic);
	              sampleClient.setCallback(new Client());
	              sampleClient.subscribe(topic, qos);
              }
              else
              {
            	  System.out.println("Incorrect selection. Enter q to disconnect\n p to publish\n s to subscribe\n");            	  
              }
              strSelection = oScanner.next();
            }
            sampleClient.unsubscribe(topic);
            sampleClient.disconnect();
            System.out.println("Disconnected");
            System.exit(0);
        } catch(MqttException me) {
            System.out.println("reason "+me.getReasonCode());
            System.out.println("msg "+me.getMessage());
            System.out.println("loc "+me.getLocalizedMessage());
            System.out.println("cause "+me.getCause());
            System.out.println("excep "+me);
            me.printStackTrace();
        }
    }
    public void connectionLost(Throwable cause) {
        // TODO Auto-generated method stub

    }

    public void messageArrived(String topic, MqttMessage message)
            throws Exception {
     System.out.println("Message Received : " + message);   
    }

    public void deliveryComplete(IMqttDeliveryToken token) {
        // TODO Auto-generated method stub

    }
}