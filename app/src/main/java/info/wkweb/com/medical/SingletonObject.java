package info.wkweb.com.medical;

import org.json.JSONArray;

/**
 * Created by learntodrill on 20/02/20.
 */

public class SingletonObject
{

    public  static  SingletonObject instance;


    private JSONArray Array_Addcart,Array_product;
    private String str_updatecart,str_revieworder,str_updatehomecart,str_ordercancel,str_count;

    public  static  SingletonObject Instance()
    {
        if (instance==null)
        {
            instance=new SingletonObject();
        }
        return  instance;
    }
    public  String getstr_count()
    {
        return  str_count;
    }
    public  void  setstr_count(String str_count)
    {
        this.str_count=str_count;
    }

    public  JSONArray getArray_product()
    {
        return  Array_product;
    }
    public  void  setArray_product(JSONArray Array_product)
    {
        this.Array_product=Array_product;
    }

    public  JSONArray getArray_Addcart()
    {
        return  Array_Addcart;
    }
    public  void  setArray_Addcart(JSONArray Array_Addcart)
    {
        this.Array_Addcart=Array_Addcart;
    }
    public  String getstr_updatecart()
    {
        return  str_updatecart;
    }
    public  void  setstr_updatecart(String str_updatecart)
    {
        this.str_updatecart=str_updatecart;
    }

    public  String getstr_revieworder()
    {
        return  str_revieworder;
    }

    public  void  setstr_revieworder(String str_revieworder)
    {
        this.str_revieworder=str_revieworder;
    }
    public  String getstr_updatehomecart()
    {
        return  str_updatehomecart;
    }
    public  void  setstr_updatehomecart(String str_updatehomecart)
    {
        this.str_updatehomecart=str_updatehomecart;
    }
    public  String getstr_ordercancel()
    {
        return  str_ordercancel;
    }
    public  void  setstr_ordercancel(String str_ordercancel)
    {
        this.str_ordercancel=str_ordercancel;
    }


}
