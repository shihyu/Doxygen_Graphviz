#pragma once

#include "SaleModel.h"

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace decorator
			{
				namespace example6
				{
					///
					/// <summary> * ��Ʒ���۹����ҵ��ӿ� </summary>
					/// 
					class GoodsSaleEbi
					{
					///	
					///	 <summary> * ����������Ϣ��������������Ӧ���Ƕ�����̫�鷳�ˣ�Ϊ����ʾ���򵥵� </summary>
					///	 * <param name="user"> ������Ա </param>
					///	 * <param name="customer"> �ͻ� </param>
					///	 * <param name="saleModel"> �������� </param>
					///	 * <returns> �Ƿ񱣴�ɹ� </returns>
					///	 
					public:
						virtual public bool sale(std::string, std::string, SaleModel*) = 0;
					};

				}
			}
		}
	}
}