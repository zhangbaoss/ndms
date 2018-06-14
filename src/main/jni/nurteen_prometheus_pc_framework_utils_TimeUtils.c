#include <jni.h>
#include <time.h>

/*
 * Class:     nurteen_prometheus_pc_framework_utils_TimeUtils
 * Method:    currentTimeNsecs
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL Java_nurteen_prometheus_pc_framework_utils_TimeUtils_currentTimeNsecs(JNIEnv *env, jclass class) {
  struct timespec ts;
  if (clock_gettime(CLOCK_REALTIME, &ts) != 0) {
    return -1;
  }

  jlong nsec = ts.tv_sec;
  nsec = nsec * 1000000000L + ts.tv_nsec;
  return nsec;
}
