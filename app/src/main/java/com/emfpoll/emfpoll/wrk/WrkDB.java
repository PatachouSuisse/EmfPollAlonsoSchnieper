package com.emfpoll.emfpoll.wrk;

import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;

import com.emfpoll.emfpoll.beans.Choice;
import com.emfpoll.emfpoll.beans.Question;
import com.emfpoll.emfpoll.beans.Survey;
import com.emfpoll.emfpoll.beans.Vote;
import com.mysql.jdbc.Statement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

/**
 * Created by alonsolopeza on 29.01.2018.
 */

public class WrkDB {

    private final String HOST = "schniepern.emf-informatique.ch";
    private final String PORT = "3306";
    private final String SCHEMA = "schniepern_emfpoll";
    private final String USER = "schniepern_emfpoll";
    private final String PASSWORD = "emf+po123ll";
    private static final String LOG_TAG = WrkDB.class.getSimpleName();

    private Connection con;

    private WrkDB() {
        try {
            if (con == null || con.isClosed()) {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                String url = "jdbc:mysql://" + HOST + ":" + PORT + "/" + SCHEMA;
                Log.d(LOG_TAG, "============================ Start DB connection ("+url+") ============================");
                con = DriverManager.getConnection(url, USER, PASSWORD);
                Log.d(LOG_TAG, "============================ End DB connection ("+con+") ============================");
            } else {
                Log.d(LOG_TAG, "============================ Connection already open ("+con+") ============================");
            }
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            Log.e(LOG_TAG, ex.getMessage());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    private static class Holder {
        private static final WrkDB INSTANCE;

        static {
            WrkDB temp = null;
            try {
                //Check if in UI thread
                if(Looper.myLooper() == Looper.getMainLooper()) {
                    temp = new ConnectTask().execute().get();
                } else {
                    temp = new WrkDB();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            INSTANCE = temp;
        }

        private static class ConnectTask extends AsyncTask<Void, Void, WrkDB> {
            @Override
            protected WrkDB doInBackground(Void... voids) {
                return new WrkDB();
            }
        }

    }

    public static WrkDB getInstance() {
        return Holder.INSTANCE;
    }

    public ResultSet select(String query, Object... o) throws SQLException {
        PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        Log.d(LOG_TAG, "***************// Sql select executed ("+ps+") //***************");
        return preparePreparedStatement(ps, o).executeQuery();
    }

    public ResultSet insert(String query, Object... o) throws SQLException {
        PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        Log.d(LOG_TAG, "***************// Sql insert executed ("+query+") //***************");
        preparePreparedStatement(ps, o).executeUpdate();
        return ps.getGeneratedKeys();
    }

    public int insertCount(String query, Object... o) throws SQLException {
        PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        return preparePreparedStatement(ps, o).executeUpdate();
    }

    /*public int delete(String query, Object... o) throws SQLException {
        PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        Log.d(LOG_TAG, "*************** Sql delete executed ("+query+") ***************");
        return preparePreparedStatement(ps, o).executeUpdate();
}

    public int update(String query, Object... o) throws SQLException {
        Log.d(LOG_TAG, "*************** Sql update executed ("+query+") ***************");
        return delete(query, o);
    }*/

    private PreparedStatement preparePreparedStatement(PreparedStatement ps, Object... objects) {
        int i = 1;
        for (Object o : objects) {
            try {
                if(o == null) {
                    ps.setObject(i, null);
                } else {
                    if (o instanceof String) {
                        ps.setString(i, (String) o);
                    } else if (o instanceof Integer) {
                        ps.setInt(i, (int) o);
                    } else if (o instanceof Long) {
                        ps.setLong(i, (long) o);
                    } else if (o instanceof Double) {
                        ps.setDouble(i, (double) o);
                    } else if (o instanceof Boolean) {
                        ps.setBoolean(i, (boolean) o);
                    } else if (o instanceof Date) {
                        java.sql.Timestamp d = new java.sql.Timestamp(((Date) o).getTime());
                        ps.setTimestamp(i, d);
                    }/* else if (o instanceof java.sql.Date) {
                        ps.setDate(i, (java.sql.Date) o);
                    }*/
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            i++;
        }
        return ps;
    }

    public boolean vote(ArrayList<Vote> votes) {
        Log.i(LOG_TAG, "================ INSERTION VOTE... =================");
        boolean success = false;
        try {
            con.setAutoCommit(false);
            for(Vote vote : votes) {
                int count = insertCount("INSERT INTO r_vote (fk_choice, visitorid) VALUES (?,?)", vote.getChoice().getPkChoice(), vote.getVisitorid());
                Log.d(LOG_TAG, count + "");
                if(count == 0) {
                    Log.i(LOG_TAG, "================ PAS INSERE =================");
                    return false;
                }
            }
            Log.i(LOG_TAG, "================ CHOIX INSERES =================");
            return true;
        } catch (SQLException ex) {
            Log.e(LOG_TAG, "Exception //public boolean vote(ArrayList<Vote> votes)// : "+ex.getMessage());
        } finally {
            try {
                con.setAutoCommit(true);
            } catch (SQLException ex) {
                Log.e(LOG_TAG, "Exception //public boolean vote(ArrayList<Vote> votes)// : "+ex.getMessage());
            }
        }
        return false;
    }

    public Integer createSurvey(Survey survey) {
        try {
            con.setAutoCommit(false);
            ResultSet surveyKey = insert("INSERT INTO t_survey (creatorid, name, start) VALUES (?,?,?)",
                    survey.getCreatorid(),
                    survey.getName(),
                    survey.getStart());
            if(surveyKey.first()) {
                Log.i(LOG_TAG, "================ SURVEY INSERE =================");
                int fkSurvey = surveyKey.getInt(1);
                for(Question question : survey.getQuestions()) {
                    Log.i(LOG_TAG, "================ QUESTION " + question + " =================");
                    ResultSet questionKey = insert("INSERT INTO t_question (fk_survey, title) VALUES (?,?)",
                            fkSurvey,
                            question.getTitle());
                    if(questionKey.first()) {
                        Log.i(LOG_TAG, "================ QUESTION INSERE =================");
                        int fkQuestion = questionKey.getInt(1);
                        for(Choice choice : question.getChoices()) {
                            Log.i(LOG_TAG, "================ CHOIX " + choice + " =================");
                            ResultSet rs = insert("INSERT INTO t_choice (fk_question, text, multiple) VALUES (?,?,?)",
                                    fkQuestion,
                                    choice.getText(),
                                    choice.isMultiple());
                            if(!rs.first()) {
                                Log.i(LOG_TAG, "================ CHOIX PAS INSERE =================");
                                con.rollback();
                                return null;
                            }
                            Log.i(LOG_TAG, "================ CHOIX INSERE =================");
                        }
                    } else {
                        Log.e(LOG_TAG, "================ QUESTION PAS INSERE =================");
                        con.rollback();
                        return null;
                    }
                }
                return fkSurvey;
            } else {
                Log.e(LOG_TAG, "================ SURVEY PAS INSERE =================");
                con.rollback();
                return null;
            }
        } catch (SQLException ex) {
            Log.e(LOG_TAG, "Exception //public Integer createSurvey(Survey survey)// : "+ex.getMessage());
        } finally {
            try {
                con.setAutoCommit(true);
            } catch (SQLException ex) {
                Log.e(LOG_TAG, "Exception //public Integer createSurvey(Survey survey)// : "+ex.getMessage());
            }
        }
        return null;
    }

    /**
     * Retourne un sondage identifié par l'argument.
     *
     * @param pkSurvey L'identifiant unique du sondage
     * @return Le sondage à récupérer
     */
    public Survey getSurvey(int pkSurvey) {
        Survey survey = null;
        try {
            ResultSet rsSurvey = select("SELECT creatorid, name, start, end FROM t_survey WHERE pk_survey = ?", pkSurvey);
            if(rsSurvey.first()) {
                ArrayList<Question> questions = new ArrayList<>();
                survey = new Survey(pkSurvey,
                        questions,
                        rsSurvey.getString("name"),
                        rsSurvey.getDate("start"),
                        rsSurvey.getDate("end"),
                        rsSurvey.getString("creatorid"));
                ResultSet rsQuestions = select("SELECT pk_question, title FROM t_question WHERE fk_survey = ?", pkSurvey);
                while(rsQuestions.next()) {
                    ArrayList<Choice> choices = new ArrayList<>();
                    Question question = new Question(rsQuestions.getInt("pk_question"),
                            choices,
                            rsQuestions.getString("title"),
                            survey);
                    ResultSet rsChoices = select("SELECT pk_choice, fk_question, text, multiple FROM t_choice WHERE fk_question = ?", rsQuestions.getInt("pk_question"));
                    while(rsChoices.next()) {
                        ArrayList<Vote> votes = new ArrayList<>();
                        Choice choice = new Choice(rsChoices.getInt("pk_choice"),
                                rsChoices.getString("text"),
                                rsChoices.getBoolean("multiple"),
                                question,
                                votes);
                        ResultSet rsVotes = select("SELECT visitorid FROM r_votes WHERE fk_choice = ?", rsChoices.getInt("pk_choice"));
                        while(rsVotes.next()) {
                            votes.add(new Vote(choice, rsChoices.getString("visitorid")));
                        }
                        choices.add(choice);
                    }
                    questions.add(question);
                }
            }
        } catch (SQLException e) {
            Log.e(LOG_TAG, "Exception // public Survey getSurvey(int pkSurvey)// : "+e.getMessage());
        }
        return survey;
    }

    /**
     * Retourne une liste des sondages d'un créateur (sans questions ou choix associées);
     *
     * @param creatorId L'identifiant du créateur
     * @return La liste des sondages demandés
     * @throws SQLException SqlException
     */
    public ArrayList<Survey> getSurveyList(String creatorId) {
        ArrayList<Survey> surveys = null;
        try {
            ResultSet rsSurvey = select("SELECT pk_survey, name, start, end FROM t_survey WHERE creatorid = ?", creatorId);
            surveys = new ArrayList<>();
            while(rsSurvey.next()) {
                surveys.add(new Survey(rsSurvey.getInt("pk_survey"),
                        null,
                        rsSurvey.getString("name"),
                        rsSurvey.getDate("start"),
                        rsSurvey.getDate("end"),
                        creatorId));
            }
        } catch (SQLException e) {
            Log.e(LOG_TAG, "Exception //public ArrayList<Survey> getSurveyList(String creatorId) throws SQLException// : "+e.getMessage());
        }
        return surveys;
    }

}
