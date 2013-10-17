#pragma once

#include <string>
#include <vector>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace templatemethod
			{
				namespace example6
				{

					///
					/// <summary> * һ���򵥵�ʵ��JDBC��ɾ�Ĳ鹦�ܵ�ģ�� </summary>
					/// 
					class JDBCTemplate
					{
					///	
					///	 <summary> * ���嵱ǰ�Ĳ������������� </summary>
					///	 
					protected:
						static const int TYPE_CREATE = 1;
					///	
					///	 <summary> * ���嵱ǰ�Ĳ����������޸� </summary>
					///	 
						static const int TYPE_UPDATE = 2;
					///	
					///	 <summary> * ���嵱ǰ�Ĳ���������ɾ�� </summary>
					///	 
						static const int TYPE_DELETE = 3;
					///	
					///	 <summary> * ���嵱ǰ�Ĳ��������ǰ�������ѯ </summary>
					///	 
						static const int TYPE_CONDITION = 4;

					/*---------------------ģ�巽��---------------------*/	
					///	
					///	 <summary> * ʵ�������Ĺ��� </summary>
					///	 * <param name="obj"> ��Ҫ�����������ݶ��� </param>
					///	 
					public:
						void create(object *obj);
					///	
					///	 <summary> * ʵ���޸ĵĹ��� </summary>
					///	 * <param name="obj"> ��Ҫ���޸ĵ����ݶ��� </param>
					///	 
						void update(object *obj);
					///	
					///	 <summary> * ʵ��ɾ���Ĺ��� </summary>
					///	 * <param name="obj"> ��Ҫ��ɾ�������ݶ��� </param>
					///	 
						void delete(object *obj);
					///	
					///	 <summary> * ʵ�ְ���������ѯ�Ĺ��� </summary>
					///	 * <param name="qm"> ��װ��ѯ���������ݶ��� </param>
					///	 * <returns> �������������ݶ��󼯺� </returns>
					///	 
						Collection *getByCondition(object *qm);


					/*---------------------ԭ�����---------------------*/		
					///	
					///	 <summary> * ��ȡ������Ҫ������sql </summary>
					///	 * <param name="type"> �������� </param>
					///	 * <returns> ������Ӧ������sql </returns>
					///	 
					protected:
						virtual std::string getMainSql(int type) = 0;
					///	
					///	 <summary> * Ϊ���²�����sql�е�"?"����ֵ </summary>
					///	 * <param name="type"> �������� </param>
					///	 * <param name="pstmt"> PreparedStatement���� </param>
					///	 * <param name="obj"> ���������ݶ��� </param>
					///	 * <exception cref="Exception"> </exception>
					///	 
						virtual void setUpdateSqlValue(int type, PreparedStatement *pstmt, object *obj) = 0 throw(Exception);

					///	
					///	 <summary> * Ϊͨ�ò�ѯ��̬��ƴ��sql���������֣�����˼·�ǣ�
					///	 * ֻ���û���д����Ӧ����������ô����sql����Ӷ�Ӧ������ </summary>
					///	 * <param name="sql"> sql�����ɲ��� </param>
					///	 * <param name="qm"> ��װ��ѯ����������ģ�� </param>
					///	 * <returns> ƴ�Ӻõ�sql��� </returns>
					///	 
						virtual std::string prepareQuerySql(std::string sql, object *qm) = 0;
					///	
					///	 <summary> * Ϊͨ�ò�ѯ��sql��̬����������ֵ </summary>
					///	 * <param name="pstmt"> Ԥ�����ѯsql�Ķ��� </param>
					///	 * <param name="qm"> ��װ��ѯ����������ģ�� </param>
					///	 * <exception cref="Exception"> </exception>
					///	 
						virtual void setQuerySqlValue(PreparedStatement *pstmt, object *qm) = 0 throw(Exception);
					///	
					///	 <summary> * �Ѳ�ѯ���صĽ����ת����Ϊ���ݶ��� </summary>
					///	 * <param name="rs"> ��ѯ���صĽ���� </param>
					///	 * <returns> ��ѯ���صĽ����ת����Ϊ���ݶ��� </returns>
					///	 * <exception cref="Exception"> </exception>
					///	 
						virtual object *rs2Object(ResultSet *rs) = 0 throw(Exception);

					/*---------------------���Ӳ���---------------------*/		
					///	
					///	 <summary> * �������ݿ��Ĭ��ʵ�֣����Ա����า�� </summary>
					///	 * <returns> ���ݿ����� </returns>
					///	 * <exception cref="Exception"> </exception>
					///	 
						virtual Connection *getConnection() throw(Exception);
					///	
					///	 <summary> * ִ�в�ѯ </summary>
					///	 * <param name="sql"> ��ѯ������sql��� </param>
					///	 * <param name="qm"> ��װ��ѯ����������ģ�� </param>
					///	 * <returns> ��ѯ��Ľ�����󼯺� </returns>
					///	 
						virtual Collection *getByCondition(std::string sql, object *qm);
					///	
					///	 <summary> * ִ�и������ݵ�sql��䣬������ɾ�ĵĹ��� </summary>
					///	 * <param name="sql"> ��Ҫִ�е�sql��� </param>
					///	 * <param name="callback"> �ص��ӿڣ��ص�Ϊsql��丳ֵ�ķ��� </param>
					///	 
						virtual void executeUpdate(std::string sql, int type, object *obj);
					};

				}
			}
		}
	}
}