package org.ornate.extension

/**
 * Path marker. Use to replace item defined in your uri path.
 * E.g. if path is "/book/{hotelId}/{roomNumber}" you can do like this:
 *
 * <pre>`= "book/{hotelId}/{roomNumber}")
 * MainPage onMainPage(@Path("hotelId") long id, @Path("roomNumber") String number);
`</pre> *
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class Path(val value: String)
