#pragma once

#include "Iterator.h"
#include "ConcreteAggregate.h"

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace iterator
			{
				namespace example1
				{

					///
					/// <summary> * ���������ʵ�ֶ���ʾ����Ǿۺ϶���Ϊ����ĵ�����
					/// * ��ͬ�ľۺ϶�����Ӧ�ĵ�����ʵ���ǲ�һ���� </summary>
					/// 
					class ConcreteIterator : public Iterator
					{
					///	
					///	 <summary> * ���б������ľ���ľۺ϶��� </summary>
					///	 
					private:
						ConcreteAggregate *aggregate;
					///	
					///	 <summary> * �ڲ���������¼��ǰ������������λ�á�
					///	 * -1��ʾ�տ�ʼ��ʱ�򣬵�����ָ��ۺ϶����һ������֮ǰ </summary>
					///	 
						int index;
					///	
					///	 <summary> * ���췽�������뱻�����ľ���ľۺ϶��� </summary>
					///	 * <param name="aggregate"> �������ľ���ľۺ϶��� </param>
					///	 
					public:
						ConcreteIterator(ConcreteAggregate *aggregate);

						virtual void first();
						virtual void next();
						virtual bool isDone();
						virtual object *currentItem();

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