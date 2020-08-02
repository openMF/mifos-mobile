package org.mifos.mobile

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader

import java.io.InputStreamReader

/**
 * Created by dilpreet on 26/6/17.
 */
class TestDataFactory {
    /**
     * Note : This Generic Method DeSerialize Only Json Object in POJO
     *
     *
     * Note : Do Not use Array [] in POJO classes for of any element initialization,
     * Use Instead ArrayList.
     *
     * @param model    Class of the Model of the Pojo
     * @param jsonName Name of Json file in test/resource
     * @param <T>      Return type
     * @return Return the Object Type model by Deserializing the Json of resources
     * @Example Of Deserializing Object Type Json
     *
     *
     * Object object = mTestDataFactory.getListTypePojo(
     * new TypeToken<Object>(){}, "Object.json")
    </Object></T> */
    fun <T> getObjectTypePojo(model: Class<T>?, jsonName: String?): T {
        val `in` = javaClass.classLoader.getResourceAsStream(jsonName)
        val reader = JsonReader(InputStreamReader(`in`))
        return Gson().fromJson(reader, model)
    }

    /**
     * Note : This Generic Method DeSerialize Both Object and List Type Json in POJO
     *
     *
     * Note : Do Not use Array [] in POJO classes for of any element initialization,
     * Use Instead ArrayList.
     *
     * @param listModel Class of the List Model
     * @param jsonName  Name of the Json in resources
     * @param <T>       return type
     * @return Return the List of the listModel by Deserializing the Json of resources
     * @Example of Deserializing List Type Json
     *
     *
     * TestDataFactory mTestDataFactory = new TestDataFactory();
     *
     *
     * List<Object> listObject = mTestDataFactory.getListTypePojo(
     * new TypeToken<List></List><Object>>(){}, "ListObject.json")
     * @Example Of Deserializing Object Type Json
     *
     *
     * Object object = mTestDataFactory.getListTypePojo(
     * new TypeToken<Object>(){}, "Object.json")
    </Object></Object></Object></T> */
    fun <T> getListTypePojo(listModel: TypeToken<T?>?, jsonName: String?): T {
        val `in` = javaClass.classLoader.getResourceAsStream(jsonName)
        val reader = JsonReader(InputStreamReader(`in`))
        return Gson().fromJson(reader, listModel?.type)
    }
}