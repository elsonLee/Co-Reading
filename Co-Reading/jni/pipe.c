#include <jni.h>
#include <stdio.h>
#include <unistd.h>
#include <android/log.h>

// get all field
/*
jclass jCls = (*env)->GetObjectClass(env, class_fdesc);
//jmethodID midGetFields = (*env)->GetMethodID(env, jCls, "getMethods", "()[Ljava/lang/reflect/Method;");
//getFields only get public fields!
//jmethodID midGetFields = (*env)->GetMethodID(env, jCls, "getFields", "()[Ljava/lang/reflect/Field;");
jmethodID midGetFields = (*env)->GetMethodID(env, jCls, "getDeclaredFields", "()[Ljava/lang/reflect/Field;");

jobjectArray jobjArray = (jobjectArray)(*env)->CallObjectMethod(env, jCls, midGetFields);

jsize len = (*env)->GetArrayLength(env, jobjArray);
__android_log_print(ANDROID_LOG_DEBUG, "JNI", "len = %d", len);

jsize i;
for (i = 0; i < len; i++) {
    jobject _strMethod = (*env)->GetObjectArrayElement(env, jobjArray, i);
    jclass _methodClazz = (*env)->GetObjectClass(env, _strMethod);
    jmethodID mid = (*env)->GetMethodID(env, _methodClazz, "getName", "()Ljava/lang/String;");
    jstring _name = (jstring) (*env)->CallObjectMethod(env, _strMethod, mid);
    char buf[128];
    const char* str = (*env)->GetStringUTFChars(env, _name, 0);
    __android_log_print(ANDROID_LOG_DEBUG, "JNI", "\n%s", str);
    (*env)->ReleaseStringUTFChars(env, _name, str);
}
*/

jobjectArray
Java_com_example_pipe_MainActivity_createpipe(
        JNIEnv*  env,
        jobject  this )
{
	jobjectArray retArray;
	jfieldID field_id;
	jmethodID const_fdesc;
	jclass class_fdesc, class_ioex;
	jint fd[2];

	class_ioex = (*env)->FindClass(env, "java/io/IOException");
	if (NULL == class_ioex)
		return NULL;

	class_fdesc = (*env)->FindClass(env, "java/io/FileDescriptor");
	if (NULL == class_fdesc)
		return NULL;

	retArray = (jobjectArray)(*env)->NewObjectArray(env, 2, class_fdesc, NULL);

	if (pipe(fd) != 0) {
		return NULL;
	}

	// construct a new FileDescriptor
	const_fdesc = (*env)->GetMethodID(env, class_fdesc, "<init>", "()V");
	if (NULL == const_fdesc)
		return NULL;
	jobject objfd0 = (*env)->NewObject(env, class_fdesc, const_fdesc);
	jobject objfd1 = (*env)->NewObject(env, class_fdesc, const_fdesc);

	field_id = (*env)->GetFieldID(env, class_fdesc, "descriptor", "I");
	if (NULL == field_id)
		return NULL;

	(*env)->SetIntField(env, objfd0, field_id, fd[0]);
	(*env)->SetIntField(env, objfd1, field_id, fd[1]);

	(*env)->SetObjectArrayElement(env, retArray, 0, objfd0);
	(*env)->SetObjectArrayElement(env, retArray, 1, objfd1);

	return retArray;
}

void
Java_com_example_pipe_MainActivity_closefd(
        JNIEnv*  env,
        jobject  this,
        jobject  filedesc) {

	jclass class_fdesc;
	jfieldID field_id;
	jint fd = -1;

	class_fdesc = (*env)->GetObjectClass(env, filedesc);
	if (NULL == class_fdesc) {
		return;
	}

	field_id = (*env)->GetFieldID(env, class_fdesc, "descriptor", "I");
	if (NULL == field_id) {
		return;
	}

	fd = (*env)->GetIntField(env, filedesc, field_id);
	if (fd == -1)
		return;

	close(fd);
	__android_log_print(ANDROID_LOG_DEBUG, "JNI", "Close file: %d\n", (int)fd);

	return;
}

