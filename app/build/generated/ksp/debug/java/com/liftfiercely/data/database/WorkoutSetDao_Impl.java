package com.liftfiercely.data.database;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.liftfiercely.data.model.WorkoutSet;
import java.lang.Class;
import java.lang.Double;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
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
public final class WorkoutSetDao_Impl implements WorkoutSetDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<WorkoutSet> __insertionAdapterOfWorkoutSet;

  private final SharedSQLiteStatement __preparedStmtOfDeleteSet;

  private final SharedSQLiteStatement __preparedStmtOfDeleteSetsForWorkout;

  public WorkoutSetDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfWorkoutSet = new EntityInsertionAdapter<WorkoutSet>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `workout_sets` (`id`,`workoutId`,`exerciseId`,`reps`,`weight`,`targetReps`,`overallSetNumber`,`timestamp`,`setNumber`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final WorkoutSet entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getWorkoutId());
        statement.bindString(3, entity.getExerciseId());
        statement.bindLong(4, entity.getReps());
        statement.bindDouble(5, entity.getWeight());
        statement.bindLong(6, entity.getTargetReps());
        statement.bindLong(7, entity.getOverallSetNumber());
        statement.bindLong(8, entity.getTimestamp());
        statement.bindLong(9, entity.getSetNumber());
      }
    };
    this.__preparedStmtOfDeleteSet = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM workout_sets WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteSetsForWorkout = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM workout_sets WHERE workoutId = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertSet(final WorkoutSet workoutSet,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfWorkoutSet.insertAndReturnId(workoutSet);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteSet(final long id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteSet.acquire();
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
          __preparedStmtOfDeleteSet.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteSetsForWorkout(final long workoutId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteSetsForWorkout.acquire();
        int _argIndex = 1;
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
          __preparedStmtOfDeleteSetsForWorkout.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<WorkoutSet>> getSetsForWorkoutFlow(final long workoutId) {
    final String _sql = "SELECT * FROM workout_sets WHERE workoutId = ? ORDER BY timestamp ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, workoutId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"workout_sets"}, new Callable<List<WorkoutSet>>() {
      @Override
      @NonNull
      public List<WorkoutSet> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfWorkoutId = CursorUtil.getColumnIndexOrThrow(_cursor, "workoutId");
          final int _cursorIndexOfExerciseId = CursorUtil.getColumnIndexOrThrow(_cursor, "exerciseId");
          final int _cursorIndexOfReps = CursorUtil.getColumnIndexOrThrow(_cursor, "reps");
          final int _cursorIndexOfWeight = CursorUtil.getColumnIndexOrThrow(_cursor, "weight");
          final int _cursorIndexOfTargetReps = CursorUtil.getColumnIndexOrThrow(_cursor, "targetReps");
          final int _cursorIndexOfOverallSetNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "overallSetNumber");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfSetNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "setNumber");
          final List<WorkoutSet> _result = new ArrayList<WorkoutSet>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final WorkoutSet _item;
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
            _item = new WorkoutSet(_tmpId,_tmpWorkoutId,_tmpExerciseId,_tmpReps,_tmpWeight,_tmpTargetReps,_tmpOverallSetNumber,_tmpTimestamp,_tmpSetNumber);
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
  public Object getSetsForWorkout(final long workoutId,
      final Continuation<? super List<WorkoutSet>> $completion) {
    final String _sql = "SELECT * FROM workout_sets WHERE workoutId = ? ORDER BY timestamp ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, workoutId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<WorkoutSet>>() {
      @Override
      @NonNull
      public List<WorkoutSet> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfWorkoutId = CursorUtil.getColumnIndexOrThrow(_cursor, "workoutId");
          final int _cursorIndexOfExerciseId = CursorUtil.getColumnIndexOrThrow(_cursor, "exerciseId");
          final int _cursorIndexOfReps = CursorUtil.getColumnIndexOrThrow(_cursor, "reps");
          final int _cursorIndexOfWeight = CursorUtil.getColumnIndexOrThrow(_cursor, "weight");
          final int _cursorIndexOfTargetReps = CursorUtil.getColumnIndexOrThrow(_cursor, "targetReps");
          final int _cursorIndexOfOverallSetNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "overallSetNumber");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfSetNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "setNumber");
          final List<WorkoutSet> _result = new ArrayList<WorkoutSet>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final WorkoutSet _item;
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
            _item = new WorkoutSet(_tmpId,_tmpWorkoutId,_tmpExerciseId,_tmpReps,_tmpWeight,_tmpTargetReps,_tmpOverallSetNumber,_tmpTimestamp,_tmpSetNumber);
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
  public Object getSetCountForExercise(final long workoutId, final String exerciseId,
      final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM workout_sets WHERE workoutId = ? AND exerciseId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, workoutId);
    _argIndex = 2;
    _statement.bindString(_argIndex, exerciseId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
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
  public Object getTotalSetCount(final long workoutId,
      final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM workout_sets WHERE workoutId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, workoutId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
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
  public Object findExactHistoricalMatch(final String exerciseId, final int overallSetNumber,
      final int targetReps, final Continuation<? super WorkoutSet> $completion) {
    final String _sql = "\n"
            + "        SELECT ws.* FROM workout_sets ws\n"
            + "        INNER JOIN workouts w ON ws.workoutId = w.id\n"
            + "        WHERE ws.exerciseId = ? \n"
            + "        AND ws.overallSetNumber = ? \n"
            + "        AND ws.targetReps = ?\n"
            + "        AND w.isActive = 0\n"
            + "        ORDER BY w.startTime DESC\n"
            + "        LIMIT 1\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    _statement.bindString(_argIndex, exerciseId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, overallSetNumber);
    _argIndex = 3;
    _statement.bindLong(_argIndex, targetReps);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<WorkoutSet>() {
      @Override
      @Nullable
      public WorkoutSet call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfWorkoutId = CursorUtil.getColumnIndexOrThrow(_cursor, "workoutId");
          final int _cursorIndexOfExerciseId = CursorUtil.getColumnIndexOrThrow(_cursor, "exerciseId");
          final int _cursorIndexOfReps = CursorUtil.getColumnIndexOrThrow(_cursor, "reps");
          final int _cursorIndexOfWeight = CursorUtil.getColumnIndexOrThrow(_cursor, "weight");
          final int _cursorIndexOfTargetReps = CursorUtil.getColumnIndexOrThrow(_cursor, "targetReps");
          final int _cursorIndexOfOverallSetNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "overallSetNumber");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfSetNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "setNumber");
          final WorkoutSet _result;
          if (_cursor.moveToFirst()) {
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
            _result = new WorkoutSet(_tmpId,_tmpWorkoutId,_tmpExerciseId,_tmpReps,_tmpWeight,_tmpTargetReps,_tmpOverallSetNumber,_tmpTimestamp,_tmpSetNumber);
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
  public Object findHistoricalSetsForExercise(final String exerciseId, final int targetReps,
      final Continuation<? super List<WorkoutSet>> $completion) {
    final String _sql = "\n"
            + "        SELECT ws.* FROM workout_sets ws\n"
            + "        INNER JOIN workouts w ON ws.workoutId = w.id\n"
            + "        WHERE ws.exerciseId = ? \n"
            + "        AND ws.targetReps = ?\n"
            + "        AND w.isActive = 0\n"
            + "        ORDER BY w.startTime DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, exerciseId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, targetReps);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<WorkoutSet>>() {
      @Override
      @NonNull
      public List<WorkoutSet> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfWorkoutId = CursorUtil.getColumnIndexOrThrow(_cursor, "workoutId");
          final int _cursorIndexOfExerciseId = CursorUtil.getColumnIndexOrThrow(_cursor, "exerciseId");
          final int _cursorIndexOfReps = CursorUtil.getColumnIndexOrThrow(_cursor, "reps");
          final int _cursorIndexOfWeight = CursorUtil.getColumnIndexOrThrow(_cursor, "weight");
          final int _cursorIndexOfTargetReps = CursorUtil.getColumnIndexOrThrow(_cursor, "targetReps");
          final int _cursorIndexOfOverallSetNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "overallSetNumber");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfSetNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "setNumber");
          final List<WorkoutSet> _result = new ArrayList<WorkoutSet>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final WorkoutSet _item;
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
            _item = new WorkoutSet(_tmpId,_tmpWorkoutId,_tmpExerciseId,_tmpReps,_tmpWeight,_tmpTargetReps,_tmpOverallSetNumber,_tmpTimestamp,_tmpSetNumber);
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
  public Object getBestWeightForExercise(final String exerciseId,
      final Continuation<? super Double> $completion) {
    final String _sql = "\n"
            + "        SELECT MAX(ws.weight) FROM workout_sets ws\n"
            + "        INNER JOIN workouts w ON ws.workoutId = w.id\n"
            + "        WHERE ws.exerciseId = ? \n"
            + "        AND ws.reps >= ws.targetReps\n"
            + "        AND w.isActive = 0\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, exerciseId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Double>() {
      @Override
      @Nullable
      public Double call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Double _result;
          if (_cursor.moveToFirst()) {
            final Double _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getDouble(0);
            }
            _result = _tmp;
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
  public Object getBestAssistedWeightForExercise(final String exerciseId,
      final Continuation<? super Double> $completion) {
    final String _sql = "\n"
            + "        SELECT MIN(ws.weight) FROM workout_sets ws\n"
            + "        INNER JOIN workouts w ON ws.workoutId = w.id\n"
            + "        WHERE ws.exerciseId = ? \n"
            + "        AND ws.reps >= ws.targetReps\n"
            + "        AND w.isActive = 0\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, exerciseId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Double>() {
      @Override
      @Nullable
      public Double call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Double _result;
          if (_cursor.moveToFirst()) {
            final Double _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getDouble(0);
            }
            _result = _tmp;
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
  public Object getBestWeightForExerciseAtSet(final String exerciseId, final int overallSetNumber,
      final Continuation<? super Double> $completion) {
    final String _sql = "\n"
            + "        SELECT MAX(ws.weight) FROM workout_sets ws\n"
            + "        INNER JOIN workouts w ON ws.workoutId = w.id\n"
            + "        WHERE ws.exerciseId = ? \n"
            + "        AND ws.overallSetNumber = ?\n"
            + "        AND ws.reps >= ws.targetReps\n"
            + "        AND w.isActive = 0\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, exerciseId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, overallSetNumber);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Double>() {
      @Override
      @Nullable
      public Double call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Double _result;
          if (_cursor.moveToFirst()) {
            final Double _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getDouble(0);
            }
            _result = _tmp;
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
  public Object getBestAssistedWeightForExerciseAtSet(final String exerciseId,
      final int overallSetNumber, final Continuation<? super Double> $completion) {
    final String _sql = "\n"
            + "        SELECT MIN(ws.weight) FROM workout_sets ws\n"
            + "        INNER JOIN workouts w ON ws.workoutId = w.id\n"
            + "        WHERE ws.exerciseId = ? \n"
            + "        AND ws.overallSetNumber = ?\n"
            + "        AND ws.reps >= ws.targetReps\n"
            + "        AND w.isActive = 0\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, exerciseId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, overallSetNumber);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Double>() {
      @Override
      @Nullable
      public Double call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Double _result;
          if (_cursor.moveToFirst()) {
            final Double _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getDouble(0);
            }
            _result = _tmp;
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
  public Object getSuccessfulSetsInRange(final long startTime, final long endTime,
      final Continuation<? super List<WorkoutSet>> $completion) {
    final String _sql = "\n"
            + "        SELECT ws.* FROM workout_sets ws\n"
            + "        INNER JOIN workouts w ON ws.workoutId = w.id\n"
            + "        WHERE w.isActive = 0\n"
            + "        AND w.startTime >= ? \n"
            + "        AND w.startTime < ?\n"
            + "        AND ws.reps >= ws.targetReps\n"
            + "        ORDER BY w.startTime DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, startTime);
    _argIndex = 2;
    _statement.bindLong(_argIndex, endTime);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<WorkoutSet>>() {
      @Override
      @NonNull
      public List<WorkoutSet> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfWorkoutId = CursorUtil.getColumnIndexOrThrow(_cursor, "workoutId");
          final int _cursorIndexOfExerciseId = CursorUtil.getColumnIndexOrThrow(_cursor, "exerciseId");
          final int _cursorIndexOfReps = CursorUtil.getColumnIndexOrThrow(_cursor, "reps");
          final int _cursorIndexOfWeight = CursorUtil.getColumnIndexOrThrow(_cursor, "weight");
          final int _cursorIndexOfTargetReps = CursorUtil.getColumnIndexOrThrow(_cursor, "targetReps");
          final int _cursorIndexOfOverallSetNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "overallSetNumber");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfSetNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "setNumber");
          final List<WorkoutSet> _result = new ArrayList<WorkoutSet>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final WorkoutSet _item;
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
            _item = new WorkoutSet(_tmpId,_tmpWorkoutId,_tmpExerciseId,_tmpReps,_tmpWeight,_tmpTargetReps,_tmpOverallSetNumber,_tmpTimestamp,_tmpSetNumber);
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
  public Object getBestWeightForExerciseBefore(final String exerciseId, final long beforeTime,
      final Continuation<? super Double> $completion) {
    final String _sql = "\n"
            + "        SELECT MAX(ws.weight) FROM workout_sets ws\n"
            + "        INNER JOIN workouts w ON ws.workoutId = w.id\n"
            + "        WHERE ws.exerciseId = ? \n"
            + "        AND ws.reps >= ws.targetReps\n"
            + "        AND w.isActive = 0\n"
            + "        AND ws.timestamp < ?\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, exerciseId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, beforeTime);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Double>() {
      @Override
      @Nullable
      public Double call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Double _result;
          if (_cursor.moveToFirst()) {
            final Double _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getDouble(0);
            }
            _result = _tmp;
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
  public Object getBestAssistedWeightForExerciseBefore(final String exerciseId,
      final long beforeTime, final Continuation<? super Double> $completion) {
    final String _sql = "\n"
            + "        SELECT MIN(ws.weight) FROM workout_sets ws\n"
            + "        INNER JOIN workouts w ON ws.workoutId = w.id\n"
            + "        WHERE ws.exerciseId = ? \n"
            + "        AND ws.reps >= ws.targetReps\n"
            + "        AND w.isActive = 0\n"
            + "        AND ws.timestamp < ?\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, exerciseId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, beforeTime);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Double>() {
      @Override
      @Nullable
      public Double call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Double _result;
          if (_cursor.moveToFirst()) {
            final Double _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getDouble(0);
            }
            _result = _tmp;
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
