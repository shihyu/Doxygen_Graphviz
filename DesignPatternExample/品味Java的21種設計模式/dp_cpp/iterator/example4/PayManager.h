#pragma once

#include "PayModel.h"
#include <vector>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace iterator
			{
				namespace example4
				{
					///
					/// <summary> * �ͻ������еĹ��ʹ������ </summary>
					/// 
					class PayManager
					{
					private:
						std::vector<PayModel*> list;
					///	
					///	 <summary> * ��ȡ�����б� </summary>
					///	 * <returns> �����б� </returns>
					///	 
					public:
						virtual std::vector<PayModel*> getPayList();
					///	
					///	 <summary> * ���㹤�ʣ���ʵӦ���кܶ������Ϊ����ʾ�Ӽ� </summary>
					///	 
						virtual void calcPay();

					private:
						bool initialized;
						void InitializeInstanceFields()
						{
							if ( ! initialized)
							{
								list = std::vector<PayModel*>();

								initialized = true;
							}
						}

public:
	PayManager()
	{
		InitializeInstanceFields();
	}
					};

				}
			}
		}
	}
}