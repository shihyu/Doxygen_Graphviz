#pragma once

#include "SaleHandler.h"
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
					/// <summary> * �����������۵�ҵ���ܵ�ְ����� </summary>
					/// 
					class SaleMgr : public SaleHandler
					{
					public:
						virtual bool sale(std::string user, std::string customer, SaleModel *saleModel);
					};

				}
			}
		}
	}
}