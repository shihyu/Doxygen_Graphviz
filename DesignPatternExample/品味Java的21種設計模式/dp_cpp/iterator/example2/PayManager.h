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
				namespace example2
				{
					///
					/// <summary> * �ͻ������еĹ��ʹ������ </summary>
					/// 
					class PayManager
					{
					///	
					///	 <summary> * �ۺ϶���������Java�ļ��϶��� </summary>
					///	 
					private:
						std::vector list;
					///	
					///	 <summary> * ��ȡ�����б� </summary>
					///	 * <returns> �����б� </returns>
					///	 
					public:
						virtual std::vector getPayList();
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
								list = std::vector();

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