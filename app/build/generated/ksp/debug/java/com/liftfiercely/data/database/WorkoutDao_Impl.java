package com.liftfiercely.data.database;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.LongSparseArray;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.RelationUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.liftfiercely.data.model.Workout;
import com.liftfiercely.data.model.WorkoutSet;
import com.liftfiercely.data.model.WorkoutWithSets;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class WorkoutDao_Impl implements WorkoutDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Workout> __insertionAdapterOfWorkout;

  private final EntityDeletionOrUpdateAdapter<Workout> __updateAdapterOfWorkout;

  private final SharedSQLiteStatement __preparedStmtOfDeleteWorkout;

  private final SharedSQLiteStatement __preparedStmtOfUpdateWorkoutDate;

  public WorkoutDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfWorkout = new EntityInsertionAdapter<Workout>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `workouts` (`id`,`startTime`,`endTime`,`isActive`,`bodyWeight`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Workout entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getStartTime());
        if (entity.getEndTime() == null) {
          statement.bindNull(3);
        } else {
          statement.bindLong(3, entity.getEndTime());
        }
        final int _tmp = entity.isActive() ? 1 : 0;
        statement.bindLong(4, _tmp);
        statement.bindDouble(5, entity.getBodyWeight());
      }
    };
    this.__updateAdapterOfWorkout = new EntityDeletionOrUpdateAdapter<Workout>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `workouts` SET `id` = ?,`startTime` = ?,`endTime` = ?,`isActive` = ?,`bodyWeight` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Workout entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getStartTime());
        if (entity.getEndTime() == null) {
          statement.bindNull(3);
        } else {
          statement.bindLong(3, entity.getEndTime());
        }
        final int _tmp = entity.isActive() ? 1 : 0;
        statement.bindLong(4, _tmp);
        statement.bindDouble(5, entity.getBodyWeight());
        statement.bindLong(6, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteWorkout = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM workouts WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateWorkoutDate = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE workouts SET startTime = ?, endTime = ? WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertWorkout(final Workout workout, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfWorkout.insertAndReturnId(workout);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateWorkout(final Workout workout, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfWorkout.handle(workout);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteWorkout(final long id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteWorkout.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteWorkout.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateWorkoutDate(final long workoutId, final long newStartTime,
      final Long newEndTime, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateWorkoutDate.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, newStartTime);
        _argIndex = 2;
        if (newEndTime == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindLong(_argIndex, newEndTime);
        }
        _argIndex = 3;
        _stmt.bindLong(_argIndex, workoutId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfUpdateWorkoutDate.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object getWorkoutById(final long id, final Continuation<? super Workout> $completion) {
    final String _sql = "SELECT * FROM workouts WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Workout>() {
      @Override
      @Nullable
      public Workout call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startTime");
          final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final int _cursorIndexOfBodyWeight = CursorUtil.getColumnIndexOrThrow(_cursor, "bodyWeight");
          final Workout _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpStartTime;
            _tmpStartTime = _cursor.getLong(_cursorIndexOfStartTime);
            final Long _tmpEndTime;
            if (_cursor.isNull(_cursorIndexOfEndTime)) {
              _tmpEndTime = null;
            } else {
              _tmpEndTime = _cursor.getLong(_cursorIndexOfEndTime);
            }
            final boolean _tmpIsActive;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp != 0;
            final double _tmpBodyWeight;
            _tmpBodyWeight = _cursor.getDouble(_cursorIndexOfBodyWeight);
            _result = new Workout(_tmpId,_tmpStartTime,_tmpEndTime,_tmpIsActive,_tmpBodyWeight);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getActiveWorkout(final Continuation<? super Workout> $completion) {
    final String _sql = "SELECT * FROM workouts WHERE isActive = 1 LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Workout>() {
      @Override
      @Nullable
      public Workout call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startTime");
          final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final int _cursorIndexOfBodyWeight = CursorUtil.getColumnIndexOrThrow(_cursor, "bodyWeight");
          final Workout _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpStartTime;
            _tmpStartTime = _cursor.getLong(_cursorIndexOfStartTime);
            final Long _tmpEndTime;
            if (_cursor.isNull(_cursorIndexOfEndTime)) {
              _tmpEndTime = null;
            } else {
              _tmpEndTime = _cursor.getLong(_cursorIndexOfEndTime);
            }
            final boolean _tmpIsActive;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp != 0;
            final double _tmpBodyWeight;
            _tmpBodyWeight = _cursor.getDouble(_cursorIndexOfBodyWeight);
            _result = new Workout(_tmpId,_tmpStartTime,_tmpEndTime,_tmpIsActive,_tmpBodyWeight);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<Workout> getActiveWorkoutFlow() {
    final String _sql = "SELECT * FROM workouts WHERE isActive = 1 LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"workouts"}, new Callable<Workout>() {
      @Override
      @Nullable
      public Workout call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startTime");
          final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final int _cursorIndexOfBodyWeight = CursorUtil.getColumnIndexOrThrow(_cursor, "bodyWeight");
          final Workout _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpStartTime;
            _tmpStartTime = _cursor.getLong(_cursorIndexOfStartTime);
            final Long _tmpEndTime;
            if (_cursor.isNull(_cursorIndexOfEndTime)) {
              _tmpEndTime = null;
            } else {
              _tmpEndTime = _cursor.getLong(_cursorIndexOfEndTime);
            }
            final boolean _tmpIsActive;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp != 0;
            final double _tmpBodyWeight;
            _tmpBodyWeight = _cursor.getDouble(_cursorIndexOfBodyWeight);
            _result = new Workout(_tmpId,_tmpStartTime,_tmpEndTime,_tmpIsActive,_tmpBodyWeight);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<Workout>> getCompletedWorkoutsFlow() {
    final String _sql = "SELECT * FROM workouts WHERE isActive = 0 ORDER BY startTime DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"workouts"}, new Callable<List<Workout>>() {
      @Override
      @NonNull
      public List<Workout> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startTime");
          final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final int _cursorIndexOfBodyWeight = CursorUtil.getColumnIndexOrThrow(_cursor, "bodyWeight");
          final List<Workout> _result = new ArrayList<Workout>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Workout _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpStartTime;
            _tmpStartTime = _cursor.getLong(_cursorIndexOfStartTime);
            final Long _tmpEndTime;
            if (_cursor.isNull(_cursorIndexOfEndTime)) {
              _tmpEndTime = null;
            } else {
              _tmpEndTime = _cursor.getLong(_cursorIndexOfEndTime);
            }
            final boolean _tmpIsActive;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp != 0;
            final double _tmpBodyWeight;
            _tmpBodyWeight = _cursor.getDouble(_cursorIndexOfBodyWeight);
            _item = new Workout(_tmpId,_tmpStartTime,_tmpEndTime,_tmpIsActive,_tmpBodyWeight);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<Workout>> getAllWorkoutsFlow() {
    final String _sql = "SELECT * FROM workouts ORDER BY startTime DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"workouts"}, new Callable<List<Workout>>() {
      @Override
      @NonNull
      public List<Workout> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startTime");
          final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final int _cursorIndexOfBodyWeight = CursorUtil.getColumnIndexOrThrow(_cursor, "bodyWeight");
          final List<Workout> _result = new ArrayList<Workout>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Workout _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpStartTime;
            _tmpStartTime = _cursor.getLong(_cursorIndexOfStartTime);
            final Long _tmpEndTime;
            if (_cursor.isNull(_cursorIndexOfEndTime)) {
              _tmpEndTime = null;
            } else {
              _tmpEndTime = _cursor.getLong(_cursorIndexOfEndTime);
            }
            final boolean _tmpIsActive;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp != 0;
            final double _tmpBodyWeight;
            _tmpBodyWeight = _cursor.getDouble(_cursorIndexOfBodyWeight);
            _item = new Workout(_tmpId,_tmpStartTime,_tmpEndTime,_tmpIsActive,_tmpBodyWeight);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getWorkoutWithSets(final long id,
      final Continuation<? super WorkoutWithSets> $completion) {
    final String _sql = "SELECT * FROM workouts WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, true, _cancellationSignal, new Callable<WorkoutWithSets>() {
      @Override
      @Nullable
      public WorkoutWithSets call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startTime");
            final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
            final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
            final int _cursorIndexOfBodyWeight = CursorUtil.getColumnIndexOrThrow(_cursor, "bodyWeight");
            final LongSparseArray<ArrayList<WorkoutSet>> _collectionSets = new LongSparseArray<ArrayList<WorkoutSet>>();
            while (_cursor.moveToNext()) {
              final long _tmpKey;
              _tmpKey = _cursor.getLong(_cursorIndexOfId);
              if (!_collectionSets.containsKey(_tmpKey)) {
                _collectionSets.put(_tmpKey, new ArrayList<WorkoutSet>());
              }
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipworkoutSetsAscomLiftfiercelyDataModelWorkoutSet(_collectionSets);
            final WorkoutWithSets _result;
            if (_cursor.moveToFirst()) {
              final Workout _tmpWorkout;
              final long _tmpId;
              _tmpId = _cursor.getLong(_cursorIndexOfId);
              final long _tmpStartTime;
              _tmpStartTime = _cursor.getLong(_cursorIndexOfStartTime);
              final Long _tmpEndTime;
              if (_cursor.isNull(_cursorIndexOfEndTime)) {
                _tmpEndTime = null;
              } else {
                _tmpEndTime = _cursor.getLong(_cursorIndexOfEndTime);
              }
              final boolean _tmpIsActive;
              final int _tmp;
              _tmp = _cursor.getInt(_cursorIndexOfIsActive);
              _tmpIsActive = _tmp != 0;
              final double _tmpBodyWeight;
              _tmpBodyWeight = _cursor.getDouble(_cursorIndexOfBodyWeight);
              _tmpWorkout = new Workout(_tmpId,_tmpStartTime,_tmpEndTime,_tmpIsActive,_tmpBodyWeight);
              final ArrayList<WorkoutSet> _tmpSetsCollection;
              final long _tmpKey_1;
              _tmpKey_1 = _cursor.getLong(_cursorIndexOfId);
              _tmpSetsCollection = _collectionSets.get(_tmpKey_1);
              _result = new WorkoutWithSets(_tmpWorkout,_tmpSetsCollection);
            } else {
              _result = null;
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
            _statement.release();
          }
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<WorkoutWithSets> getWorkoutWithSetsFlow(final long id) {
    final String _sql = "SELECT * FROM workouts WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"workout_sets",
        "workouts"}, new Callable<WorkoutWithSets>() {
      @Override
      @Nullable
      public WorkoutWithSets call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startTime");
            final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
            final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
            final int _cursorIndexOfBodyWeight = CursorUtil.getColumnIndexOrThrow(_cursor, "bodyWeight");
            final LongSparseArray<ArrayList<WorkoutSet>> _collectionSets = new LongSparseArray<ArrayList<WorkoutSet>>();
            while (_cursor.moveToNext()) {
              final long _tmpKey;
              _tmpKey = _cursor.getLong(_cursorIndexOfId);
              if (!_collectionSets.containsKey(_tmpKey)) {
                _collectionSets.put(_tmpKey, new ArrayList<WorkoutSet>());
              }
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipworkoutSetsAscomLiftfiercelyDataModelWorkoutSet(_collectionSets);
            final WorkoutWithSets _result;
            if (_cursor.moveToFirst()) {
              final Workout _tmpWorkout;
              final long _tmpId;
              _tmpId = _cursor.getLong(_cursorIndexOfId);
              final long _tmpStartTime;
              _tmpStartTime = _cursor.getLong(_cursorIndexOfStartTime);
              final Long _tmpEndTime;
              if (_cursor.isNull(_cursorIndexOfEndTime)) {
                _tmpEndTime = null;
              } else {
                _tmpEndTime = _cursor.getLong(_cursorIndexOfEndTime);
              }
              final boolean _tmpIsActive;
              final int _tmp;
              _tmp = _cursor.getInt(_cursorIndexOfIsActive);
              _tmpIsActive = _tmp != 0;
              final double _tmpBodyWeight;
              _tmpBodyWeight = _cursor.getDouble(_cursorIndexOfBodyWeight);
              _tmpWorkout = new Workout(_tmpId,_tmpStartTime,_tmpEndTime,_tmpIsActive,_tmpBodyWeight);
              final ArrayList<WorkoutSet> _tmpSetsCollection;
              final long _tmpKey_1;
              _tmpKey_1 = _cursor.getLong(_cursorIndexOfId);
              _tmpSetsCollection = _collectionSets.get(_tmpKey_1);
              _result = new WorkoutWithSets(_tmpWorkout,_tmpSetsCollection);
            } else {
              _result = null;
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
          }
        } finally {
          __db.endTransaction();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<WorkoutWithSets>> getCompletedWorkoutsWithSetsFlow() {
    final String _sql = "SELECT * FROM workouts WHERE isActive = 0 ORDER BY startTime DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"workout_sets",
        "workouts"}, new Callable<List<WorkoutWithSets>>() {
      @Override
      @NonNull
      public List<WorkoutWithSets> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startTime");
            final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
            final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
            final int _cursorIndexOfBodyWeight = CursorUtil.getColumnIndexOrThrow(_cursor, "bodyWeight");
            final LongSparseArray<ArrayList<WorkoutSet>> _collectionSets = new LongSparseArray<ArrayList<WorkoutSet>>();
            while (_cursor.moveToNext()) {
              final long _tmpKey;
              _tmpKey = _cursor.getLong(_cursorIndexOfId);
              if (!_collectionSets.containsKey(_tmpKey)) {
                _collectionSets.put(_tmpKey, new ArrayList<WorkoutSet>());
              }
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipworkoutSetsAscomLiftfiercelyDataModelWorkoutSet(_collectionSets);
            final List<WorkoutWithSets> _result = new ArrayList<WorkoutWithSets>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final WorkoutWithSets _item;
              final Workout _tmpWorkout;
              final long _tmpId;
              _tmpId = _cursor.getLong(_cursorIndexOfId);
              final long _tmpStartTime;
              _tmpStartTime = _cursor.getLong(_cursorIndexOfStartTime);
              final Long _tmpEndTime;
              if (_cursor.isNull(_cursorIndexOfEndTime)) {
                _tmpEndTime = null;
              } else {
                _tmpEndTime = _cursor.getLong(_cursorIndexOfEndTime);
              }
              final boolean _tmpIsActive;
              final int _tmp;
              _tmp = _cursor.getInt(_cursorIndexOfIsActive);
              _tmpIsActive = _tmp != 0;
              final double _tmpBodyWeight;
              _tmpBodyWeight = _cursor.getDouble(_cursorIndexOfBodyWeight);
              _tmpWorkout = new Workout(_tmpId,_tmpStartTime,_tmpEndTime,_tmpIsActive,_tmpBodyWeight);
              final ArrayList<WorkoutSet> _tmpSetsCollection;
              final long _tmpKey_1;
              _tmpKey_1 = _cursor.getLong(_cursorIndexOfId);
              _tmpSetsCollection = _collectionSets.get(_tmpKey_1);
              _item = new WorkoutWithSets(_tmpWorkout,_tmpSetsCollection);
              _result.add(_item);
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
          }
        } finally {
          __db.endTransaction();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getAllWorkoutDates(final Continuation<? super List<Long>> $completion) {
    final String _sql = "SELECT startTime FROM workouts WHERE isActive = 0";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Long>>() {
      @Override
      @NonNull
      public List<Long> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final List<Long> _result = new ArrayList<Long>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Long _item;
            _item = _cursor.getLong(0);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getWorkoutsInRange(final long startTime, final long endTime,
      final Continuation<? super List<Workout>> $completion) {
    final String _sql = "SELECT * FROM workouts WHERE isActive = 0 AND startTime >= ? AND startTime < ? ORDER BY startTime DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, startTime);
    _argIndex = 2;
    _statement.bindLong(_argIndex, endTime);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Workout>>() {
      @Override
      @NonNull
      public List<Workout> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startTime");
          final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final int _cursorIndexOfBodyWeight = CursorUtil.getColumnIndexOrThrow(_cursor, "bodyWeight");
          final List<Workout> _result = new ArrayList<Workout>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Workout _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpStartTime;
            _tmpStartTime = _cursor.getLong(_cursorIndexOfStartTime);
            final Long _tmpEndTime;
            if (_cursor.isNull(_cursorIndexOfEndTime)) {
              _tmpEndTime = null;
            } else {
              _tmpEndTime = _cursor.getLong(_cursorIndexOfEndTime);
            }
            final boolean _tmpIsActive;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp != 0;
            final double _tmpBodyWeight;
            _tmpBodyWeight = _cursor.getDouble(_cursorIndexOfBodyWeight);
            _item = new Workout(_tmpId,_tmpStartTime,_tmpEndTime,_tmpIsActive,_tmpBodyWeight);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getWorkoutsWithSetsInRange(final long startTime, final long endTime,
      final Continuation<? super List<WorkoutWithSets>> $completion) {
    final String _sql = "SELECT * FROM workouts WHERE isActive = 0 AND startTime >= ? AND startTime < ? ORDER BY startTime DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, startTime);
    _argIndex = 2;
    _statement.bindLong(_argIndex, endTime);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, true, _cancellationSignal, new Callable<List<WorkoutWithSets>>() {
      @Override
      @NonNull
      public List<WorkoutWithSets> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startTime");
            final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
            final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
            final int _cursorIndexOfBodyWeight = CursorUtil.getColumnIndexOrThrow(_cursor, "bodyWeight");
            final LongSparseArray<ArrayList<WorkoutSet>> _collectionSets = new LongSparseArray<ArrayList<WorkoutSet>>();
            while (_cursor.moveToNext()) {
              final long _tmpKey;
              _tmpKey = _cursor.getLong(_cursorIndexOfId);
              if (!_collectionSets.containsKey(_tmpKey)) {
                _collectionSets.put(_tmpKey, new ArrayList<WorkoutSet>());
              }
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipworkoutSetsAscomLiftfiercelyDataModelWorkoutSet(_collectionSets);
            final List<WorkoutWithSets> _result = new ArrayList<WorkoutWithSets>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final WorkoutWithSets _item;
              final Workout _tmpWorkout;
              final long _tmpId;
              _tmpId = _cursor.getLong(_cursorIndexOfId);
              final long _tmpStartTime;
              _tmpStartTime = _cursor.getLong(_cursorIndexOfStartTime);
              final Long _tmpEndTime;
              if (_cursor.isNull(_cursorIndexOfEndTime)) {
                _tmpEndTime = null;
              } else {
                _tmpEndTime = _cursor.getLong(_cursorIndexOfEndTime);
              }
              final boolean _tmpIsActive;
              final int _tmp;
              _tmp = _cursor.getInt(_cursorIndexOfIsActive);
              _tmpIsActive = _tmp != 0;
              final double _tmpBodyWeight;
              _tmpBodyWeight = _cursor.getDouble(_cursorIndexOfBodyWeight);
              _tmpWorkout = new Workout(_tmpId,_tmpStartTime,_tmpEndTime,_tmpIsActive,_tmpBodyWeight);
              final ArrayList<WorkoutSet> _tmpSetsCollection;
              final long _tmpKey_1;
              _tmpKey_1 = _cursor.getLong(_cursorIndexOfId);
              _tmpSetsCollection = _collectionSets.get(_tmpKey_1);
              _item = new WorkoutWithSets(_tmpWorkout,_tmpSetsCollection);
              _result.add(_item);
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
            _statement.release();
          }
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object getAllCompletedWorkouts(final Continuation<? super List<Workout>> $completion) {
    final String _sql = "SELECT * FROM workouts WHERE isActive = 0 ORDER BY startTime DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Workout>>() {
      @Override
      @NonNull
      public List<Workout> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startTime");
          final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final int _cursorIndexOfBodyWeight = CursorUtil.getColumnIndexOrThrow(_cursor, "bodyWeight");
          final List<Workout> _result = new ArrayList<Workout>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Workout _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpStartTime;
            _tmpStartTime = _cursor.getLong(_cursorIndexOfStartTime);
            final Long _tmpEndTime;
            if (_cursor.isNull(_cursorIndexOfEndTime)) {
              _tmpEndTime = null;
            } else {
              _tmpEndTime = _cursor.getLong(_cursorIndexOfEndTime);
            }
            final boolean _tmpIsActive;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp != 0;
            final double _tmpBodyWeight;
            _tmpBodyWeight = _cursor.getDouble(_cursorIndexOfBodyWeight);
            _item = new Workout(_tmpId,_tmpStartTime,_tmpEndTime,_tmpIsActive,_tmpBodyWeight);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }

  private void __fetchRelationshipworkoutSetsAscomLiftfiercelyDataModelWorkoutSet(
      @NonNull final LongSparseArray<ArrayList<WorkoutSet>> _map) {
    if (_map.isEmpty()) {
      return;
    }
    if (_map.size() > RoomDatabase.MAX_BIND_PARAMETER_CNT) {
      RelationUtil.recursiveFetchLongSparseArray(_map, true, (map) -> {
        __fetchRelationshipworkoutSetsAscomLiftfiercelyDataModelWorkoutSet(map);
        return Unit.INSTANCE;
      });
      return;
    }
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT `id`,`workoutId`,`exerciseId`,`reps`,`weight`,`targetReps`,`overallSetNumber`,`timestamp`,`setNumber` FROM `workout_sets` WHERE `workoutId` IN (");
    final int _inputSize = _map.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _stmt = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (int i = 0; i < _map.size(); i++) {
      final long _item = _map.keyAt(i);
      _stmt.bindLong(_argIndex, _item);
      _argIndex++;
    }
    final Cursor _cursor = DBUtil.query(__db, _stmt, false, null);
    try {
      final int _itemKeyIndex = CursorUtil.getColumnIndex(_cursor, "workoutId");
      if (_itemKeyIndex == -1) {
        return;
      }
      final int _cursorIndexOfId = 0;
      final int _cursorIndexOfWorkoutId = 1;
      final int _cursorIndexOfExerciseId = 2;
      final int _cursorIndexOfReps = 3;
      final int _cursorIndexOfWeight = 4;
      final int _cursorIndexOfTargetReps = 5;
      final int _cursorIndexOfOverallSetNumber = 6;
      final int _cursorIndexOfTimestamp = 7;
      final int _cursorIndexOfSetNumber = 8;
      while (_cursor.moveToNext()) {
        final long _tmpKey;
        _tmpKey = _cursor.getLong(_itemKeyIndex);
        final ArrayList<WorkoutSet> _tmpRelation = _map.get(_tmpKey);
        if (_tmpRelation != null) {
          final WorkoutSet _item_1;
          final long _tmpId;
          _tmpId = _cursor.getLong(_cursorIndexOfId);
          final long _tmpWorkoutId;
          _tmpWorkoutId = _cursor.getLong(_cursorIndexOfWorkoutId);
          final String _tmpExerciseId;
          _tmpExerciseId = _cursor.getString(_cursorIndexOfExerciseId);
          final int _tmpReps;
          _tmpReps = _cursor.getInt(_cursorIndexOfReps);
          final double _tmpWeight;
          _tmpWeight = _cursor.getDouble(_cursorIndexOfWeight);
          final int _tmpTargetReps;
          _tmpTargetReps = _cursor.getInt(_cursorIndexOfTargetReps);
          final int _tmpOverallSetNumber;
          _tmpOverallSetNumber = _cursor.getInt(_cursorIndexOfOverallSetNumber);
          final long _tmpTimestamp;
          _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
          final int _tmpSetNumber;
          _tmpSetNumber = _cursor.getInt(_cursorIndexOfSetNumber);
          _item_1 = new WorkoutSet(_tmpId,_tmpWorkoutId,_tmpExerciseId,_tmpReps,_tmpWeight,_tmpTargetReps,_tmpOverallSetNumber,_tmpTimestamp,_tmpSetNumber);
          _tmpRelation.add(_item_1);
        }
      }
    } finally {
      _cursor.close();
    }
  }
}
