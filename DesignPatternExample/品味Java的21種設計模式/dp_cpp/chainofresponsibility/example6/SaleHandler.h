#pragma once

#include "SaleModel.h"
#include <string>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace chainofresponsibility
			{
				namespace example6
				{
					///
					/// <summary> * ����ְ�����Ľӿ� </summary>
					/// 
					class SaleHandler
					{
					///	
					///	 <summary> * ������һ����������Ķ��� </summary>
					///	 
					protected:
						SaleHandler *successor;
					///	
					///	 <summary> * ������һ����������Ķ��� </summary>
					///	 * <param name="successor"> ��һ����������Ķ��� </param>
					///	 
					public:
						virtual void setSuccessor(SaleHandler *successor);
					///	
					///	 <summary> * ������������Ϣ������ </summary>
					///	 * <param name="user"> ������Ա </param>
					///	 * <param name="customer"> �ͻ� </param>
					///	 * <param name="saleModel"> �������� </param>
					///	 * <returns> �Ƿ���ɹ� </returns>
					///	 
						virtual bool sale(std::string user, std::string customer, SaleModel *saleModel) = 0;
					};

				}
			}
		}
	}
}