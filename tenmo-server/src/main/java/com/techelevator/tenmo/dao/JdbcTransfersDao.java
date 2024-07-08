package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.TenmoUser;
import com.techelevator.tenmo.model.Transfers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JdbcTransfersDao implements TransferDao {

    private JdbcTemplate jdbcTemplate;
    private Logger log = LoggerFactory.getLogger(getClass());

    public JdbcTransfersDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Transfers getTransfersById(int id) {
        Transfers transfers = null;
        String sql = "SELECT * " +
                "FROM transfer " +
                "WHERE transfer_id = ?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
            if (results.next()) {
                transfers = mapToTransferpri(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return transfers;
    }

    @Override
    public List<Transfers> getTransfersByUserId(int id) {
        List<Transfers> transfers = new ArrayList<>();
        String sql = "SELECT t.transfer_id,tu.username AS from_user, tr.username as to_user, t.amount FROM transfer as t JOIN account as ac ON t.account_from = ac.account_id JOIN tenmo_user AS tu ON ac.user_id = tu.user_id JOIN account as aw ON t.account_to = aw.account_id JOIN tenmo_user as tr ON aw.user_id = tr.user_id WHERE (tu.user_id = ? OR tr.user_id = ?) AND transfer_type_id = 2 ORDER BY t.transfer_id asc;";

        SqlRowSet rowsForTable = jdbcTemplate.queryForRowSet(sql, id, id);
        if (!rowsForTable.wasNull()) {
            while (rowsForTable.next()) {
                Transfers transfers1 = mapForThird(rowsForTable);
                transfers.add(transfers1);
            }
        }


        return transfers;


    }


    @Override
    public List<Transfers> getPendingTransfers(int userId) {
        List<Transfers> transfers = new ArrayList<>();
        int accountId = 0;
        String sqlForAccount = "SELECT account_id from account WHERE user_id = ?;";
        SqlRowSet numberOne = jdbcTemplate.queryForRowSet(sqlForAccount, userId);
        if (!numberOne.wasNull()) {
            if (numberOne.next()) {
                accountId = numberOne.getInt("account_id");
            }
        }

        String sql = "SELECT transfer_id, te.username AS from_user,tl.username AS to_user, amount FROM transfer AS t JOIN account AS a ON t.account_from = a.account_id JOIN tenmo_user AS te ON a.user_id = te.user_id JOIN account as tw ON t.account_to = tw.account_id JOIN tenmo_user as tl ON tw.user_id = tl.user_id  WHERE t.account_from != ? AND t.account_to = ? AND t.transfer_status_id = 1 ORDER BY transfer_id asc;";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId, accountId);
            if (!results.wasNull()) {
                while (results.next()) {
                    Transfers newOne = mapForThird(results);

                    transfers.add(newOne);
                }
            }
        } catch (CannotGetJdbcConnectionException | DataIntegrityViolationException e) {
            throw new DaoException("Unable to get pending requests", e);
        }
        return transfers;
    }


    @Override
    public Map<Integer, String> listOf(int userId) {
        Map<Integer, String> tenmoUser = new HashMap<>();
        String sqlForprint = "SELECT user_id, username FROM tenmo_user WHERE user_id != ?;";

        SqlRowSet rowToPrint = jdbcTemplate.queryForRowSet(sqlForprint, userId);
        if (!rowToPrint.wasNull()) {
            while (rowToPrint.next()) {
                String capFirstLetter = rowToPrint.getString("username").substring(0, 1).toUpperCase();
                String remaindingLetter = rowToPrint.getString("username").substring(1);
                String value = capFirstLetter + remaindingLetter;
                tenmoUser.put(rowToPrint.getInt("user_id"), value);
            }
            return tenmoUser;
        }
        return null;
    }


    @Override
    public String sendToUser(Transfers newTransfer) {

        int accountId = 0;
        int accountIdTo = 0;

        SqlRowSet rowforint = jdbcTemplate.queryForRowSet("SELECT account_id FROM account WHERE user_id = ?", newTransfer.getUserFrom());


        if (!rowforint.wasNull()) {
            if (rowforint.next()) {
                accountId = rowforint.getInt("account_id");
            }
        }

        SqlRowSet rowforsecondInt = jdbcTemplate.queryForRowSet("SELECT account_id FROM account WHERE user_id = ?", newTransfer.getUserTo());
        if (!rowforsecondInt.wasNull()) {
            if (rowforsecondInt.next()) {
                accountIdTo = rowforsecondInt.getInt("account_id");
            } else {
                return "Please Enter an valid id, you're transaction wasn't successfully";
            }
        }

        String sqlChangeValueFrom = "UPDATE account SET balance = balance - ? WHERE account_id = ?;";

        String sqlChangeValueTo = "UPDATE account SET balance = balance + ? WHERE account_id = ?;";
        try {
            jdbcTemplate.update(sqlChangeValueFrom, newTransfer.getAmount(), accountId);
            jdbcTemplate.update(sqlChangeValueTo, newTransfer.getAmount(), accountIdTo);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }


        String updatedTransferTable = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) VALUES (2,2, ?,?,?);\n";
        jdbcTemplate.update(updatedTransferTable, accountId, accountIdTo, newTransfer.getAmount());
        String resulted = "";
        SqlRowSet finalOne = jdbcTemplate.queryForRowSet("SELECT transfer_status_desc FROM transfer_status WHERE transfer_status_id = 2;");
        if (!finalOne.wasNull()) {
            if (finalOne.next()) {
                resulted += finalOne.getString("transfer_status_desc");
            }
        }


        return "*" + resulted + "*";
    }


    @Override
    public String confirmation(Transfers transferRequest) {
        String resulted = "";
        int accountIdFrom = 0;
        int accountIdTo = 0;
        String acccountId = "SELECT account_id FROM account WHERE user_id = ?; ";
        SqlRowSet gettingAccountId = jdbcTemplate.queryForRowSet(acccountId, transferRequest.getUserFrom());
        if (!gettingAccountId.wasNull()) {
            if (gettingAccountId.next()) {
                accountIdFrom = gettingAccountId.getInt("account_id");
            } else {
                return "Please enter an valid id, You're transaction wasn't successfully";
            }
        }
        SqlRowSet getAcountIdTo = jdbcTemplate.queryForRowSet(acccountId, transferRequest.getUserTo());
        if (!getAcountIdTo.wasNull()) {
            if (getAcountIdTo.next()) {
                accountIdTo = getAcountIdTo.getInt("account_id");
            } else {
                return "Please Enter an valid id, you're transaction wasn't successfully";
            }
        }

        String confirmedStatus = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) VALUES (1,1,?,?,?)";


        jdbcTemplate.update(confirmedStatus, accountIdFrom, accountIdTo, transferRequest.getAmount());
        String lastSqlStatement = "SELECT transfer_status_desc FROM transfer_status WHERE transfer_status_id = ?;";
        SqlRowSet returnAnswer = jdbcTemplate.queryForRowSet(lastSqlStatement, 1);
        if (!returnAnswer.wasNull()) {
            if (returnAnswer.next()) {
                resulted = returnAnswer.getString("transfer_status_desc");
            }
        }
        return "*" + resulted + "*";

    }

    @Override
    public String approved(int transferId){
        String sqlStatement ="UPDATE transfer SET transfer_type_id = 2, transfer_status_id = 2 WHERE transfer_id = ?;";
        jdbcTemplate.update(sqlStatement,transferId);
        BigDecimal amountBalance = BigDecimal.valueOf(0);
        int accountFrom = 0;
        int accountTo = 0;
        String amountget = "SELECT amount FROM transfer WHERE transfer_id = ?;";
        SqlRowSet sqlForAmount = jdbcTemplate.queryForRowSet(amountget,transferId);
        if(!sqlForAmount.wasNull()){
            if(sqlForAmount.next()){
                amountBalance = sqlForAmount.getBigDecimal("amount");
            }
        }
        String accountFromGet = "SELECT account_from FROM transfer WHERE transfer_id = ?;";
        SqlRowSet sqlforAccount = jdbcTemplate.queryForRowSet(accountFromGet,transferId);
        if(!sqlforAccount.wasNull()){
            if(sqlforAccount.next()){
                accountFrom = sqlforAccount.getInt("account_from");
            }
        }
        String accountToGet = "SELECT account_to FROM transfer WHERE transfer_id = ?;";
        SqlRowSet sqltoAccount = jdbcTemplate.queryForRowSet(accountToGet,transferId);
        if(!sqltoAccount.wasNull()){
            if(sqltoAccount.next()){
                accountTo = sqltoAccount.getInt("account_to");
            }
        }
        String firstUpdate = "UPDATE account SET balance = balance - ? WHERE account_id = ?;";
        String secondUpdate = "UPDATE account SET balance = balance + ? WHERE account_id = ?;";

        jdbcTemplate.update(firstUpdate,amountBalance,accountTo);
        jdbcTemplate.update(secondUpdate,amountBalance,accountFrom);
        String confirmed = "SELECT transfer_status_desc FROM transfer_status as ts\n" +
                "JOIN transfer as t ON ts.transfer_status_id = t.transfer_status_id\n" +
                "WHERE t.transfer_id = ?;";
        String status = "";
        SqlRowSet resulted = jdbcTemplate.queryForRowSet(confirmed,transferId);
        if(!resulted.wasNull()){
            if(resulted.next()){
                status = resulted.getString("transfer_status_desc");
            }
        }

        return "*"+status+"*";
    }

    @Override
    public String rejected(int transferId) {
        String deletedFrom = "DELETE FROM transfer WHERE transfer_id = ?;";
        int deleteCount = jdbcTemplate.update(deletedFrom,transferId);
        if(deleteCount==1){
            return "* Rejected *";
        }
        List<Integer>hello = new ArrayList<>();


        return "null";
    }



    public Transfers mapToTransferSet(SqlRowSet sqlRowSet) {
        Transfers transfers = new Transfers();
        transfers.setTransferId(sqlRowSet.getInt("transfer_id"));
        transfers.setAccountFrom(sqlRowSet.getInt("account_from"));
        transfers.setAccountTo(sqlRowSet.getInt("account_to"));
        transfers.setAmount(sqlRowSet.getBigDecimal("amount"));
        transfers.setTransferStatusId(sqlRowSet.getInt("transfer_status_id"));
        transfers.setTransferTypeId(sqlRowSet.getInt("transfer_type_id"));
        if (sqlRowSet.findColumn("from_username") == 0) {
            System.out.println("wontWork");
            transfers.setFromUsername(sqlRowSet.getString("from_username"));
        }
        if (sqlRowSet.findColumn("to_username") >= 1) {
            transfers.setToUsername(sqlRowSet.getString("to_username"));
        }
        return transfers;

    }

    public Transfers mapForSecondRow(SqlRowSet sqlRowSet) {
        Transfers transfers = new Transfers();
        transfers.setTransferId(sqlRowSet.getInt("transfer_id"));
        transfers.setTransferStatusId(sqlRowSet.getInt("transfer_status_id"));
        transfers.setFromUsername(sqlRowSet.getString("from_username"));
        transfers.setToUsername(sqlRowSet.getString("to_username"));
        transfers.setAmount(sqlRowSet.getBigDecimal("amount"));
        return transfers;
    }

    public Transfers mapForThird(SqlRowSet sqlRowSet) {
        Transfers transfers = new Transfers();
        transfers.setTransferId(sqlRowSet.getInt("transfer_id"));
        transfers.setFromUsername(sqlRowSet.getString("from_user"));
        transfers.setToUsername(sqlRowSet.getString("to_user"));
        transfers.setAmount(sqlRowSet.getBigDecimal("amount"));
        return transfers;
    }

    public Transfers mapToTransferpri(SqlRowSet sqlRowSet) {
        Transfers transfers = new Transfers();
        transfers.setTransferId(sqlRowSet.getInt("transfer_id"));
        transfers.setAccountFrom(sqlRowSet.getInt("account_from"));
        transfers.setAccountTo(sqlRowSet.getInt("account_to"));
        transfers.setAmount(sqlRowSet.getBigDecimal("amount"));
        transfers.setTransferStatusId(sqlRowSet.getInt("transfer_status_id"));
        transfers.setTransferTypeId(sqlRowSet.getInt("transfer_type_id"));
        return transfers;
    }



}







