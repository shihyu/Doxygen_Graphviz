#pragma once

#include "Iterator.h"
#include "SalaryManager.h"

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace iterator
			{
				namespace example7
				{

					///
					/// <summary> * ����ʵ�ַ��������˫������ӿ� </summary>
					/// 
					class ArrayIteratorImpl : public Iterator
					{
					///	
					///	 <summary> * ������ű������ľۺ϶��� </summary>
					///	 
					private:
						SalaryManager *aggregate;
					///	
					///	 <summary> * ������¼��ǰ��������λ������
					///	 * -1��ʾ�տ�ʼ��ʱ�򣬵�����ָ��ۺ϶����һ������֮ǰ </summary>
					///	 
						int index;

					public:
						ArrayIteratorImpl(SalaryManager *aggregate);

						virtual void first();
						virtual void next();
						virtual bool isDone();
						virtual object *currentItem();

						virtual bool isFirst();
						virtual void previous();

					private:
						bool initialized;
						void InitializeInstanceFields()
						{
							if ( ! initialized)
							{
								index = -1;

								initialized = true;
							}
						}
					};

				}
			}
		}
	}
}