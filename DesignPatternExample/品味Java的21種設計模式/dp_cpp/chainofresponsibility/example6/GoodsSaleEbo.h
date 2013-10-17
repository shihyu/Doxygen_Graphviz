#pragma once

#include "SaleModel.h"
#include "SaleSecurityCheck.h"
#include "SaleDataCheck.h"
#include "SaleLogicCheck.h"
#include "SaleMgr.h"
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
					/// <summary> * ��Ʒ���۹���ģ���ҵ���� </summary>
					/// 
					class GoodsSaleEbo
					{
					///	
					///	 <summary> * ����������Ϣ��������������Ӧ���Ƕ�����̫�鷳�ˣ�Ϊ����ʾ���򵥵� </summary>
					///	 * <param name="user"> ������Ա </param>
					///	 * <param name="customer"> �ͻ� </param>
					///	 * <param name="saleModel"> �������� </param>
					///	 * <returns> �Ƿ񱣴�ɹ� </returns>
					///	 
					public:
						virtual bool sale(std::string user, std::string customer, SaleModel *saleModel);
					};

				}
			}
		}
	}
}