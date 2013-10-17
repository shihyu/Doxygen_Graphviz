#pragma once

#include "Leaf.h"
#include <string>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace composite
			{
				namespace example1
				{

					///
					/// <summary> * ��϶��󣬿��԰���������϶������Ҷ�Ӷ��� </summary>
					/// 
					class Composite
					{
					///	
					///	 <summary> * ������¼������������϶��� </summary>
					///	 
					private:
						Collection<Composite*> *childComposite;
					///	
					///	 <summary> * ������¼����������Ҷ�Ӷ��� </summary>
					///	 
						Collection<Leaf*> *childLeaf;
					///	
					///	 <summary> * ��϶�������� </summary>
					///	 
						std::string name;
					///	
					///	 <summary> * ���췽����������϶�������� </summary>
					///	 * <param name="name"> ��϶�������� </param>
					///	 
					public:
						Composite(std::string name);
					///	
					///	 <summary> * ����϶�����뱻��������������϶��� </summary>
					///	 * <param name="c"> ����������������϶��� </param>
					///	 
						virtual void addComposite(Composite *c);
					///	
					///	 <summary> * ����϶�����뱻��������Ҷ�Ӷ��� </summary>
					///	 * <param name="leaf"> ����������Ҷ�Ӷ��� </param>
					///	 
						virtual void addLeaf(Leaf *leaf);
					///	
					///	 <summary> * �����϶�������Ľṹ </summary>
					///	 * <param name="preStr"> ǰ׺����Ҫ�ǰ��ղ㼶ƴ�ӵĿո�ʵ��������� </param>
					///	 
						virtual void printStruct(std::string preStr);

					private:
						bool initialized;
						void InitializeInstanceFields()
						{
							if ( ! initialized)
							{
								childComposite = std::vector<Composite*>();
								childLeaf = std::vector<Leaf*>();
								name = "";

								initialized = true;
							}
						}
					};

				}
			}
		}
	}
}