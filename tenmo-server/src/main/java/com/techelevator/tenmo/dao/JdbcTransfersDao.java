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
                transfers = mapToTransferSet(results);
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

        String sql = "SELECT t.transfer_id, t.transfer_status_id, t.transfer_type_id,  afu.username AS from_username, atu.username AS to_username , t.amount " +
                "FROM transfer t " +
                "JOIN account af ON t.account_from = af.account_id " +
                "JOIN tenmo_user afu ON af.user_id = afu.user_id " +
                "JOIN account at ON t.account_to = at.account_id " +
                "JOIN tenmo_user atu ON at.user_id = atu.user_id " +
                "WHERE af.user_id = ?  OR at.user_id = ? ";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id, id);

                if (!results.wasNull()) {
                    if (results.next()) {
                        Transfers newOne = new Transfers();
                        newOne.setFromUsername(results.getString("from_username"));
                        newOne.setToUsername(results.getString("to_username"));
                        newOne.setAmount(results.getBigDecimal("amount"));
                        newOne.setTransferId(results.getInt("transfer_id"));
                        newOne.setTransferStatusId(results.getInt("transfer_status_id"));
                        newOne.setTransferTypeId(results.getInt("transfer_type_id"));
                        System.out.println(newOne.getAmount());
                        transfers.add(newOne);
                    }
                }

            } catch(CannotGetJdbcConnectionException e){
                throw new DaoException("Unable to connect to server or database", e);
            } catch(DataIntegrityViolationException e){
                throw new DaoException("Data integrity violation", e);
            }
            return transfers;
        }



        @Override
        public List<Transfers> getPendingTransfers (int userId){
            List<Transfers> transfers = new ArrayList<>();
            String sql = "SELECT t.transfer_id, t.account_from, t.account_to, t.amount, t.transfer_status_id, t.transfer_type_id, afu.username AS from_username, atu.username AS to_username " +
                    "FROM transfer t " +
                    "JOIN account af ON t.account_from = af.account_id " +
                    "JOIN tenmo_user afu ON af.user_id = afu.user_id " +
                    "JOIN account at ON t.account_to = at.account_id " +
                    "JOIN tenmo_user atu ON at.user_id = atu.user_id " +
                    "WHERE (af.user_id = ? OR at.user_id = ?) AND t.transfer_status_id = 1";
            try {
                SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId, userId);
                if (!results.wasNull()) {
                    if (results.next()) {
                        Transfers transfer = new Transfers();
                        transfer.setTransferId(results.getInt("transfer_id"));
                        transfer.setAccountFrom(results.getInt("account_from"));
                        transfer.setAccountTo(results.getInt("account_to"));
                        transfer.setAmount(results.getBigDecimal("amount"));
                        transfer.setTransferStatusId(results.getInt("transfer_status_id"));
                        transfer.setTransferTypeId(results.getInt("transfer_type_id"));
                        transfer.setFromUsername(results.getString("from_username"));
                        transfer.setToUsername(results.getString("to_username"));
                        transfers.add(transfer);
                    }
                }
            } catch (CannotGetJdbcConnectionException | DataIntegrityViolationException e) {
                throw new DaoException("Unable to get pending requests", e);
            }
            return transfers;

        }

        @Override
        public Map<Integer, String> listOf ( int userId){
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
        public String sendToUser (Transfers newTransfer){
            int accountId = 0;
            int accountIdTo = 0;
            SqlRowSet rowforint = jdbcTemplate.queryForRowSet("SELECT account_id FROM account WHERE user_id = ?", newTransfer.getAccountFrom());
            if (!rowforint.wasNull()) {
                if (rowforint.next()) {
                    accountId = rowforint.getInt("account_id");
                }
            }
            SqlRowSet rowforsecondInt = jdbcTemplate.queryForRowSet("SELECT account_id FROM account WHERE user_id = ?", newTransfer.getAccountTo());
            if (!rowforsecondInt.wasNull()) {
                if (rowforsecondInt.next()) {
                    accountIdTo = rowforsecondInt.getInt("account_id");
                }
            }


            String sqlChangeValueFrom = "UPDATE account SET balance = balance - ? WHERE account_id = ?;";
            String sqlChangeValueTo = "UPDATE account SET balance = balance + ? WHERE account_id = ?;";
            jdbcTemplate.update(sqlChangeValueFrom, newTransfer.getAmount(), accountId);
            jdbcTemplate.update(sqlChangeValueTo, newTransfer.getAmount(), accountIdTo);
            String updatedTransferTable = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) VALUES (2,2, ?,?,?);\n";
            jdbcTemplate.update(updatedTransferTable, accountId, accountIdTo, newTransfer.getAmount());
            String resulted = "";
            SqlRowSet finalOne = jdbcTemplate.queryForRowSet("SELECT transfer_status_desc FROM transfer_status WHERE transfer_status_id = 2;");
            if (!finalOne.wasNull()) {
                if (finalOne.next()) {
                    System.out.println(resulted += finalOne.getString("transfer_status_desc"));
                }
            }


            return "*" + resulted + "*";
        }


        public Transfers mapToTransferSet (SqlRowSet sqlRowSet){
            Transfers transfers = new Transfers();
            transfers.setTransferId(sqlRowSet.getInt("transfer_id"));
            transfers.setTransferStatusId(sqlRowSet.getInt("transfer_status_id"));
            transfers.setAccountFrom(sqlRowSet.getInt("account_from"));
            transfers.setAccountTo(sqlRowSet.getInt("account_to"));
            transfers.setAmount(sqlRowSet.getBigDecimal("amount"));
            transfers.setFromUsername(sqlRowSet.getString("from_username"));
            transfers.setToUsername(sqlRowSet.getString("to_username"));
            return transfers;

        }

        private TenmoUser mapToTenmoUser (SqlRowSet sqlRowSet){
            TenmoUser tenmoUser = new TenmoUser();
            tenmoUser.setUsername(sqlRowSet.getString("username"));
            tenmoUser.setUserId(sqlRowSet.getInt("user_id"));
            tenmoUser.setPasswordHash(sqlRowSet.getString("password_hash"));
            return tenmoUser;
        }
    }

