#pragma once

#include "UserModel.h"
#include "UserQueryModel.h"
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


					class JDBCTest
					{
					private:
						Connection *getConnection() throw(Exception);

					public:
						virtual void create(UserModel *um);
						virtual Collection *getByCondition(UserQueryModel *uqm);
					///	
					///	 <summary> * Ϊͨ�ò�ѯ��̬��ƴ��sql���������֣�����˼·�ǣ�
					///	 * ����û���д����Ӧ����������ô����sql����Ӷ�Ӧ������ </summary>
					///	 * <param name="sql"> sql�����ɲ��� </param>
					///	 * <param name="uqm"> ��װ��ѯ����������ģ�� </param>
					///	 * <returns> ƴ�Ӻõ�sql��� </returns>
					///	 
					private:
						std::string prepareSql(std::string sql, UserQueryModel *uqm);
					///	
					///	 <summary> * Ϊͨ�ò�ѯ��sql��̬����������ֵ </summary>
					///	 * <param name="pstmt"> Ԥ�����ѯsql�Ķ��� </param>
					///	 * <param name="uqm"> ��װ��ѯ����������ģ�� </param>
					///	 * <exception cref="Exception"> </exception>
					///	 
						void setValue(PreparedStatement *pstmt, UserQueryModel *uqm) throw(Exception);
					///	
					///	 <summary> * �Ѳ�ѯ���صĽ����ת����Ϊ���� </summary>
					///	 * <param name="rs"> ��ѯ���صĽ���� </param>
					///	 * <returns> ��ѯ���صĽ����ת����Ϊ���� </returns>
					///	 * <exception cref="Exception"> </exception>
					///	 
						UserModel *rs2Object(ResultSet *rs) throw(Exception);

						static void main(std::string args[]);
					};

				}
			}
		}
	}
}