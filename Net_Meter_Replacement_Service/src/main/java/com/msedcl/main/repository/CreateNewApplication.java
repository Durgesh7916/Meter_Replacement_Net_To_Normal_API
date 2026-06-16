/*
 * package com.msedcl.main.repository;
 * 
 * import javax.sql.DataSource;
 * 
 * import org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.stereotype.Service; import
 * org.springframework.transaction.annotation.Transactional;
 * 
 * import com.msedcl.main.dto.MeterReplacementRequestDto;
 * 
 * 
 * 
 * @Service
 * 
 * @Transactional public class CreateNewApplication {
 * 
 * @Autowired private DataSource dataSource;
 * 
 * public String createMeterReplacement( MeterReplacementRequestDto request) {
 * 
 * try {
 * 
 * // ================================================== // STEP 1 : VALIDATIONS
 * // ==================================================
 * 
 * String consumerNumber = request.getConsumerDetails() .getConsumerNumber();
 * 
 * ConsumerExistsValidator.validate( dataSource, consumerNumber);
 * 
 * PendingApplicationValidator.validate( dataSource, consumerNumber);
 * 
 * OldMeterValidator.validate( dataSource, consumerNumber);
 * 
 * NewMeterValidator.validate( dataSource, request.getNewMeterDetails()
 * .getNewMeterNo());
 * 
 * MeterAssignedValidator.validate( dataSource, request.getNewMeterDetails()
 * .getNewMeterNo());
 * 
 * 
 * 
 * SameMeterValidator.validate( request.getOldMeterReading() .getOldMeterNo(),
 * request.getNewMeterDetails() .getNewMeterNo());
 * 
 * // ================================================== // STEP 2 : CREATE
 * APPLICATION // ==================================================
 * 
 * Long applicationId = createApplication( consumerNumber,
 * request.getConsumerDetails() .getUpdatedBy());
 * 
 * // ================================================== // STEP 3 : SAVE OLD
 * METER READING // ==================================================
 * 
 * saveOldMeterReading( applicationId, request.getOldMeterReading());
 * 
 * // ================================================== // STEP 4 : SAVE NEW
 * METER ASSIGNMENT // ==================================================
 * 
 * Long meterAssignId = saveNewMeterAssignment( applicationId, request);
 * 
 * // ================================================== // STEP 5 : SAVE
 * INITIAL READING // ==================================================
 * 
 * saveInitialReading( meterAssignId, request.getNewMeterReading());
 * 
 * return "Application Created Successfully. " + "Application Id = " +
 * applicationId;
 * 
 * } catch (Exception e) {
 * 
 * throw new RuntimeException( e.getMessage()); } }
 * 
 * // ====================================================== // CREATE
 * APPLICATION // ======================================================
 * 
 * private Long createApplication( String consumerNumber, String user) {
 * 
 * String sql = "INSERT INTO NC_METER_REPLACE_APPLICATION " +
 * "(APPLICATION_ID_N," + " APPLN_SERVICE_TYPE_ID_N," +
 * " ASSIGNED_CONSUMER_NO_C," + " CURRENT_WF_STATUS_ID_N," + " STATUS_CD_C," +
 * " CREATED_BY_C," + " CREATED_DT)" + " VALUES " + "(SEQ_APP_ID.NEXTVAL," +
 * " 32," + " ?," + " 31," + " 'A'," + " ?," + " SYSDATE)";
 * 
 * Long applicationId = null;
 * 
 * try ( Connection con = dataSource.getConnection(); PreparedStatement ps =
 * con.prepareStatement(sql) ) {
 * 
 * ps.setString(1, consumerNumber); ps.setString(2, user);
 * 
 * ps.executeUpdate();
 * 
 * applicationId = getCurrentApplicationId(con);
 * 
 * return applicationId;
 * 
 * } catch (Exception e) {
 * 
 * throw new RuntimeException( "Error creating application : " +
 * e.getMessage()); } }
 * 
 * // ====================================================== // GET APPLICATION
 * ID // ======================================================
 * 
 * private Long getCurrentApplicationId( Connection con) throws Exception {
 * 
 * String sql = "SELECT SEQ_APP_ID.CURRVAL " + "FROM DUAL";
 * 
 * try ( PreparedStatement ps = con.prepareStatement(sql); ResultSet rs =
 * ps.executeQuery() ) {
 * 
 * rs.next();
 * 
 * return rs.getLong(1); } }
 * 
 * // ====================================================== // OLD METER
 * READING // ======================================================
 * 
 * private void saveOldMeterReading( Long applicationId, OldMeterReadingDto dto)
 * {
 * 
 * String sql = "INSERT INTO NC_OLD_METER_READING " + "(APPLICATION_ID_N," +
 * " OLD_METER_SR_NO_C," + " READING_DATE," + " HEADER_KWH_N," +
 * " HEADER_KVAH_N)" + " VALUES " + "(?,?,?,?,?)";
 * 
 * try ( Connection con = dataSource.getConnection(); PreparedStatement ps =
 * con.prepareStatement(sql) ) {
 * 
 * ps.setLong(1, applicationId); ps.setString(2, dto.getOldMeterNo());
 * 
 * ps.setDate( 3, java.sql.Date.valueOf( dto.getReadingDate()));
 * 
 * ps.setDouble(4, dto.getKwh()); ps.setDouble(5, dto.getKvah());
 * 
 * ps.executeUpdate();
 * 
 * } catch (Exception e) {
 * 
 * throw new RuntimeException( "Error saving old meter reading : " +
 * e.getMessage()); } }
 * 
 * // ====================================================== // NEW METER
 * ASSIGNMENT // ======================================================
 * 
 * private Long saveNewMeterAssignment( Long applicationId,
 * MeterReplacementRequestDto request) {
 * 
 * String sql = "INSERT INTO NC_METER_ASSIGN " + "(APPLICATION_ID_N," +
 * " METER_SR_NO_C," + " OLD_METER_SR_NO_C," + " METER_MAKE_CD_C," +
 * " INSTALLATION_DATE)" + " VALUES " + "(?,?,?,?,?)";
 * 
 * Long meterAssignId = 0L;
 * 
 * try ( Connection con = dataSource.getConnection(); PreparedStatement ps =
 * con.prepareStatement(sql) ) {
 * 
 * ps.setLong(1, applicationId);
 * 
 * ps.setString( 2, request.getNewMeterDetails() .getNewMeterNo());
 * 
 * ps.setString( 3, request.getOldMeterReading() .getOldMeterNo());
 * 
 * ps.setString( 4, request.getNewMeterDetails() .getMakeCode());
 * 
 * ps.setDate( 5, java.sql.Date.valueOf( request.getNewMeterDetails()
 * .getInstallationDate()));
 * 
 * ps.executeUpdate();
 * 
 * meterAssignId = getMeterAssignId( con, applicationId);
 * 
 * return meterAssignId;
 * 
 * } catch (Exception e) {
 * 
 * throw new RuntimeException( "Error saving meter assignment : " +
 * e.getMessage()); } }
 * 
 * // ====================================================== // GET ASSIGNMENT
 * ID // ======================================================
 * 
 * private Long getMeterAssignId( Connection con, Long applicationId) throws
 * Exception {
 * 
 * String sql = "SELECT MAX(METER_ASSIGNED_ID_N)" + " FROM NC_METER_ASSIGN" +
 * " WHERE APPLICATION_ID_N=?";
 * 
 * try ( PreparedStatement ps = con.prepareStatement(sql) ) {
 * 
 * ps.setLong(1, applicationId);
 * 
 * try (ResultSet rs = ps.executeQuery()) {
 * 
 * rs.next();
 * 
 * return rs.getLong(1); } } }
 * 
 * // ====================================================== // INITIAL READING
 * // ======================================================
 * 
 * private void saveInitialReading( Long meterAssignId, NewMeterReadingDto dto)
 * {
 * 
 * String sql = "INSERT INTO NC_METER_READING " + "(METER_ASSIGNED_ID_N," +
 * " METER_READING_DT," + " HEADER_KWH_N," + " HEADER_KVAH_N)" + " VALUES " +
 * "(?,?,?,?)";
 * 
 * try ( Connection con = dataSource.getConnection(); PreparedStatement ps =
 * con.prepareStatement(sql) ) {
 * 
 * ps.setLong( 1, meterAssignId);
 * 
 * ps.setDate( 2, java.sql.Date.valueOf( dto.getReadingDate()));
 * 
 * ps.setDouble( 3, dto.getInitialKwh());
 * 
 * ps.setDouble( 4, dto.getInitialKvah());
 * 
 * ps.executeUpdate();
 * 
 * } catch (Exception e) {
 * 
 * throw new RuntimeException( "Error saving initial reading : " +
 * e.getMessage()); } } }
 */