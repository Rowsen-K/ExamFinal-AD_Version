package com.rowsen.mytools;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import com.rowsen.examfinal.JudgeBean;
import com.rowsen.examfinal.SelectionBean;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/*
    目标：制作一个常用的工具类集合
    1、构造时传入一个上下文，获取程序的环境，再传入一个文件名，拿到raw目录的该文件（txt），
    最好是拿到文件对象的方式，但是现在没有找到方法，只能拿到inputStream流
    2、考试app的文件处理方法file2list，需要提供2个参数，一个是要处理的文件流，第二个是处理后需要保存到的集合对象
    3、list集合对象序列化到xml文件,2个参数，一个是需要序列化的list对象，一个是最终保存的文件对象
    4、抽题工具，随机获取一组题，选择题40个，判断题60个，形成一个list集合
    5、截图保存功能
 */
public class Tools {

    public static void file2list(InputStream in, ArrayList list,boolean mode) {//传入对应的文件的流和提供一个保存流文件提出出的数据的集合对象
        BufferedReader bf = new BufferedReader(new InputStreamReader(in));
        try {
            String s = bf.readLine();
            int n = 1;
            while (s != null && s != "" && s.contains("&")) {
                String[] all = s.split("&");
                SelectionBean sb = new SelectionBean();
                sb.No = n++ + "";
                sb.question = all[0];
                sb.answer1 = all[1];
                sb.answer2 = all[2];
                sb.answer3 = all[3];
                if(mode) {
                    sb.answer4 = all[4];
                    sb.corAns = all[5].trim();
                }
                else {
                    sb.answer4 = "空";
                    sb.corAns = all[4].trim();
                }
                list.add(sb);
                s = bf.readLine();
            }
            //Log.e(list.size()+"","-----------------------数目");
            while (s != null && s != "") {
                String[] all = s.split("=");
                JudgeBean jb = new JudgeBean();
                jb.No = n++ + "";
                jb.question = all[0];
                //Log.e(list.size()+"",jb.No+"     "+jb.question);
                jb.answer = all[1].trim();
                list.add(jb);
                s = bf.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void list2xml(List list, File f) {
        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Element root = doc.createElement("Exam");

            Element selection = doc.createElement("Selection");
            Element judge = doc.createElement("Judge");

            int n = 0;
            while (n < list.size() && list.get(n) instanceof SelectionBean) {
                SelectionBean sb = (SelectionBean) list.get(n);
                Element item = doc.createElement("Item");
                item.setAttribute("No", sb.No);
                Element ques = doc.createElement("Question");
                ques.setTextContent(sb.question);
                Element ans1 = doc.createElement("Answer1");
                ans1.setTextContent(sb.answer1);
                Element ans2 = doc.createElement("Answer2");
                ans2.setTextContent(sb.answer2);
                Element ans3 = doc.createElement("Answer3");
                ans3.setTextContent(sb.answer3);
                Element ans = doc.createElement("Answer");
                ans.setTextContent(sb.corAns);
                item.appendChild(ques);
                item.appendChild(ans1);
                item.appendChild(ans2);
                item.appendChild(ans3);
                item.appendChild(ans);
                selection.appendChild(item);
                n++;
            }
            while (n < list.size() && list.get(n) instanceof JudgeBean) {
                JudgeBean sb = (JudgeBean) list.get(n);
                Element item = doc.createElement("Item");
                item.setAttribute("No", sb.No);
                Element ques = doc.createElement("Question");
                ques.setTextContent(sb.question);
                Element ans = doc.createElement("Answer");
                ans.setTextContent(sb.answer);
                item.appendChild(ques);
                item.appendChild(ans);
                judge.appendChild(item);
                n++;
            }
            root.appendChild(selection);
            root.appendChild(judge);
            doc.appendChild(root);
            //System.out.println(doc);

            DOMSource ds = new DOMSource(doc);
            Transformer tf = TransformerFactory.newInstance().newTransformer();
            tf.transform(ds, new StreamResult(new FileOutputStream(f)));
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    /*   public static void xml2list(File xml, List list,int mode) {
           try {
               // XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
               //  parser.setInput(new FileInputStream(xml),"UTF-8");
               Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xml);
               NodeList item = doc.getElementsByTagName("Item");
               for (int n = 0; n < item.getLength(); n++) {
                   Node node = item.item(n);
                   //System.out.println(node.getParentNode().getNodeName());
                   switch (node.getParentNode().getNodeName()) {
                       case "Selection":
                          if(mode==1||mode==3) {
                              SelectionBean s = new SelectionBean();
                              s.No = node.getAttributes().item(0).getTextContent();
                              Node ques = node.getFirstChild();
                              Node ans1 = ques.getNextSibling();
                              Node ans2 = ans1.getNextSibling();
                              Node ans3 = ans2.getNextSibling();
                              Node ans = ans3.getNextSibling();
                              s.question = ques.getTextContent();
                              s.answer1 = ans1.getTextContent();
                              s.answer2 = ans2.getTextContent();
                              s.answer3 = ans3.getTextContent();
                              s.corAns = ans.getTextContent();
                              list.add(s);
                          }
                           break;
                       case "Judge":
                          if(mode==2||mode==3) {
                              JudgeBean j = new JudgeBean();
                              j.No = node.getAttributes().item(0).getTextContent();
                              Node jques = node.getFirstChild();
                              Node jans = jques.getNextSibling();
                              j.question = jques.getTextContent();
                              j.answer = jans.getTextContent();
                              list.add(j);
                          }
                           break;
                       default:
                           break;
                       // System.out.println(j);
                   }
               }
           } catch (FileNotFoundException e) {
               e.printStackTrace();
           } catch (SAXException e) {
               e.printStackTrace();
           } catch (ParserConfigurationException e) {
               e.printStackTrace();
           } catch (IOException e) {
               e.printStackTrace();
           }
       }
   */
    public static void get2question(List resource, List target, int selectionNum, int judgeNum) {
        Random r = new Random();
        int size = resource.size();
        for (int n = 1; n <= selectionNum; ) {
            Object ob = resource.get(r.nextInt(size));
            if (ob instanceof SelectionBean && !target.contains(ob)) {
                SelectionBean sb = new SelectionBean((SelectionBean) ob);
                sb.No = n + "";
                target.add(sb);
                n++;
            }
        }
        for (int n = 1; n <= judgeNum; ) {
            Object ob = resource.get(r.nextInt(size));
            if (ob instanceof JudgeBean && !target.contains(ob)) {
                JudgeBean jb = new JudgeBean((JudgeBean) ob);
                jb.No = (n + selectionNum) + "";
                target.add(jb);
                n++;
            }
        }
    }

    public static String dif2time(long dif) {
        long hour = dif / (1000 * 60 * 60);
        long minute = dif / (1000 * 60) - 60 * hour;
        return hour + ":" + minute;
    }

    // 获取指定Activity的截屏，保存到png文件
    public static Bitmap takeScreenShot(Activity activity) {
        // View是你需要截图的View
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();
        // 获取状态栏高度
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        Log.i("TAG", "" + statusBarHeight);
        // 获取屏幕长和高
        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        int height = activity.getWindowManager().getDefaultDisplay()
                .getHeight();
        // 去掉状态栏，如果需要的话
        // Bitmap b = Bitmap.createBitmap(b1, 0, 25, 320, 455);
        Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height
                - statusBarHeight);
        view.destroyDrawingCache();
        return b;
    }

    // 保存到sdcard
    private static void savePic(Bitmap b, String strFileName) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(strFileName);
            if (null != fos) {
                b.compress(Bitmap.CompressFormat.PNG, 90, fos);
                fos.flush();
                fos.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 程序入口，外界直接调用此方法即可
    public static void shoot(Activity a) {
        Tools.savePic(Tools.takeScreenShot(a), Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/Rowsen.png");
    }
}
