#pragma once

#include "Aggregate.h"
#include "PayModel.h"
#include "Iterator.h"
#include "CollectionIteratorImpl.h"
#include <vector>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace iterator
			{
				namespace example3
				{

					///
					/// <summary> * �ͻ������еĹ��ʹ������ </summary>
					/// 
					class PayManager : public Aggregate
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

						virtual Iterator *createIterator();
						virtual object *get(int index);
						virtual int size();

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