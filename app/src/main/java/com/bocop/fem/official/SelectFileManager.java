/*
 * Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.

 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 *   http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bocop.fem.official;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;

import timber.log.Timber;

/**
 * 选择文件管理器
 *
 * @author xxx
 * @since 2020-08-05
 */
public class SelectFileManager {
    private static final String TAG = "SelectFileManager";
    private static final String FILE_DOCUMENT_ID_RAW = "raw:";
    private static final String FILE_DOWNLOAD_CONTENT = "content://downloads/public_downloads";
    private static final String FILE_SCHEME_CONTENT = "content";
    private static final String FILE_SCHEME_FILE = "file";
    private static final String FILE_DOCUMENT_TYPE = "primary";
    private static final String FILE_CONTENT_TYPE_AUDIO = "audio";
    private static final String FILE_CONTENT_TYPE_IMAGE = "image";
    private static final String FILE_CONTENT_TYPE_VIDEO = "video";
    private static final String FILE_TYPE_STORAGE = "com.android.externalstorage.documents";
    private static final String FILE_TYPE_DOWNLOAD = "com.android.providers.downloads.documents";
    private static final String FILE_TYPE_MEDIA = "com.android.providers.media.documents";
    private static final String FILE_QUERY_ID = "_id=?";
    private static final String FILE_QUERY_DATA = "_data";
    private static final String FILE_SPLIT = ":";
    private static final String STRING_NULL_CONTENT = "";
    private static final String SCHEME_FILE = "/root";
    private static final int INDEX_ONE = 0;
    private static final int INDEX_TWO = 1;

    private SelectFileManager() {

    }

    /**
     * Obtain the actual file path from the file database.
     *
     * @param context    context
     * @param contentUri file URI
     * @return path of the file
     */
    public static String getFilePath(Context context, Uri contentUri) {
        String selectFilePath;
        int sdkInit = Build.VERSION.SDK_INT;
        int kitkat = Build.VERSION_CODES.KITKAT;
        boolean isKitKat = (sdkInit >= kitkat);
        if (isKitKat) {
            selectFilePath = getContentPath(context, contentUri);
        } else {
            selectFilePath = getRealPathFromUri(context, contentUri);
        }
        return selectFilePath;
    }

    /**
     * Obtain the file path.
     *
     * @param context    context
     * @param contentUri file URI
     * @return path of the file
     */
    @Nullable
    private static String getRealPathFromUri(Context context, Uri contentUri) {
        String result;
        if ((null == context) || (null == contentUri)) {
            Timber.tag(TAG).w("context or contentUri is null");
            return null;
        }
        String[] data = {MediaStore.Images.Media.DATA};
        try (Cursor cursor = context.getContentResolver().query(contentUri, data, null, null, null)) {
            if ((null != cursor) && cursor.moveToFirst()) {
                final int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                result = cursor.getString(columnIndex);
                return result;
            }
        } catch (IllegalArgumentException exception) {
            Timber.e("getRealPathFromUri IllegalArgumentException");
        }
        return null;
    }

    /**
     * Obtain the file path starting with content.
     *
     * @param context context
     * @param uri     file URI
     * @return path of the file
     */
    @Nullable
    private static String getContentPath(Context context, Uri uri) {
        String result = null;
        if ((null == context) || (null == uri)) {
            Timber.tag(TAG).w("context or uri is null");
            return null;
        }
        if (DocumentsContract.isDocumentUri(context, uri)) {
            result = dealDocument(context, uri);
        } else if (FILE_SCHEME_CONTENT.equalsIgnoreCase(uri.getScheme())) {
            Timber.i("content");
            result = getColumn(context, uri, null, null);
            return result;
        } else if (FILE_SCHEME_FILE.equalsIgnoreCase(uri.getScheme())) {
            result = uri.getPath();
            return result;
        } else {
            Timber.tag(TAG).w("the uri is other type");
        }
        return result;
    }

    /**
     * Process the Uri.
     *
     * @param context context
     * @param uri     file URI
     * @return path of the file
     */
    private static String dealDocument(Context context, Uri uri) {
        String result = null;
        if (isExternalStorage(uri)) {
            result = getExternalStorage(uri);
        } else if (isDownloads(uri)) {
            Timber.i("download");
            try {
                final String id = DocumentsContract.getDocumentId(uri);
                if (id.startsWith(FILE_DOCUMENT_ID_RAW)) {
                    result = id.replaceFirst(FILE_DOCUMENT_ID_RAW, STRING_NULL_CONTENT);
                    return result;
                }
                Uri contentUri = ContentUris.withAppendedId(Uri.parse(FILE_DOWNLOAD_CONTENT), Long.parseLong(id));
                result = getColumn(context, contentUri, null, null);
                return result;
            } catch (NumberFormatException exception) {
                Timber.e("getContentPath NumberFormatException");
            }
        } else if (isMedia(uri)) {
            String documentId = DocumentsContract.getDocumentId(uri);
            if (null != documentId) {
                result = dealWithDocumentId(context, documentId);
                return result;
            }
        } else {
            Timber.tag(TAG).w("other type");
        }
        return result;
    }

    /**
     * Return the file path based on the documentId.
     *
     * @param context    context
     * @param documentId documentId
     * @return path of the file
     */
    @Nullable
    private static String dealWithDocumentId(Context context, @NonNull String documentId) {
        String[] splits = documentId.split(FILE_SPLIT);
        String type = splits[INDEX_ONE];
        String result;
        if (null != type) {
            Uri contentUri = getContentUri(type);
            if (null == contentUri) {
                Timber.tag(TAG).w("contentUri is null");
            } else {
                result = getColumn(context, contentUri, FILE_QUERY_ID, new String[]{splits[INDEX_TWO]});
                return result;
            }
        }
        return null;
    }

    /**
     * Determine the type of the selected file.
     *
     * @param uri file URI
     * @return Whether the file is of the ExternalStorageDocument type.
     */
    private static boolean isExternalStorage(@NonNull Uri uri) {
        return FILE_TYPE_STORAGE.equals(uri.getAuthority());
    }

    /**
     * Determine the type of the selected file.
     *
     * @param uri file URI
     * @return Whether the file is of the Download type.
     */
    private static boolean isDownloads(@NonNull Uri uri) {
        return FILE_TYPE_DOWNLOAD.equals(uri.getAuthority());
    }

    /**
     * Determine the type of the selected file.
     *
     * @param uri file URI
     * @return Whether the file is of the Media type.
     */
    private static boolean isMedia(@NonNull Uri uri) {
        return FILE_TYPE_MEDIA.equals(uri.getAuthority());
    }

    /**
     * Obtain the Uri.
     *
     * @param type file type
     * @return URI corresponding to the file type
     */
    private static Uri getContentUri(String type) {
        Uri contentUri = null;
        if (FILE_CONTENT_TYPE_IMAGE.equals(type)) {
            contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        } else if (FILE_CONTENT_TYPE_VIDEO.equals(type)) {
            contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        } else if (FILE_CONTENT_TYPE_AUDIO.equals(type)) {
            contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        } else {
            Timber.tag(TAG).w("the contentUri is null");
        }
        return contentUri;
    }

    /**
     * Obtain the external storage path based on the URI.
     *
     * @param uri file URI
     * @return file external storage path
     */
    private static String getExternalStorage(Uri uri) {
        String documentId = DocumentsContract.getDocumentId(uri);
        String result = null;
        if (null != documentId) {
            String[] splits = documentId.split(FILE_SPLIT);
            String type = splits[INDEX_ONE];
            if (FILE_DOCUMENT_TYPE.equalsIgnoreCase(type)) {
                result = (Environment.getExternalStorageDirectory() + File.separator + splits[INDEX_TWO]);
            }
        }
        return result;
    }

    /**
     * Obtain the value in the data column of a URI.
     *
     * @param context       context
     * @param uri           URI to be queried
     * @param selection     filter used in the query
     * @param selectionArgs parameter used in the query
     * @return file path
     */
    @Nullable
    private static String getColumn(@NonNull Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String columnName = FILE_QUERY_DATA;
        String[] projections = {columnName};
        String result;
        try {
            cursor = context.getContentResolver().query(uri, projections, selection, selectionArgs, null);
            if ((null != cursor) && cursor.moveToFirst()) {
                result = cursor.getString(cursor.getColumnIndexOrThrow(columnName));
                return result;
            }
        } catch (IllegalArgumentException exception) {
            Timber.e("getColumn IllegalArgumentException");
            if (!TextUtils.isEmpty(uri.getPath())) {
                result = uri.getPath().replace(SCHEME_FILE, "");
                return result;
            }
        } finally {
            if (null != cursor) {
                cursor.close();
            }
        }
        return null;
    }
}
