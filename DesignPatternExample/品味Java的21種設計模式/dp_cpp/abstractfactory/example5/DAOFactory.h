#pragma once

#include "OrderMainDAO.h"
#include "OrderDetailDAO.h"

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace abstractfactory
			{
				namespace example5
				{
					///
					/// <summary> * ���󹤳����������������Ӽ�¼��Ӧ��DAO���� </summary>
					/// 
					class DAOFactory
					{
					///	
					///	 <summary> * ������������¼��Ӧ��DAO���� </summary>
					///	 * <returns> ��������¼��Ӧ��DAO���� </returns>
					///	 
					public:
						virtual OrderMainDAO *createOrderMainDAO() = 0;
					///	
					///	 <summary> * ���������Ӽ�¼��Ӧ��DAO���� </summary>
					///	 * <returns> �����Ӽ�¼��Ӧ��DAO���� </returns>
					///	 
						virtual OrderDetailDAO *createOrderDetailDAO() = 0;
					};

				}
			}
		}
	}
}